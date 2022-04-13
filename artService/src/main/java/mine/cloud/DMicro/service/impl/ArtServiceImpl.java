package mine.cloud.DMicro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.dao.VideoMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.doc.HotwordDoc;
import mine.cloud.DMicro.feignClients.ComClient;
import mine.cloud.DMicro.feignClients.UsrClient;
import mine.cloud.DMicro.mqQueueType.MqStaticType;
import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.params.RequestParamsESArt;
import mine.cloud.DMicro.params.RequestParamsRedisArtUsr;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.Comment;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.pojo.Video;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;


@Service
public class ArtServiceImpl implements IArtServiceApi  {
    //注入Feign
    @Autowired
    private UsrClient usrClient;

    @Autowired
    private ComClient comClient;
    //注入mapper
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public Article selectByPrimaryKey(Integer artId) {
        Article article = null;
        if(artId != null){
            article = articleMapper.selectByPrimaryKey(artId);
            System.out.println(article);
        }else{

        }
        return article;
    }

    @Override
    public Article selectByPKWithUsr(Integer artId) {
        Article article = articleMapper.selectByPrimaryKey(artId);
        //写死---防止报错
        String token = "123";
        //利用feign调用
        User user = (User) usrClient.selectByPK(token,article.getUsrId()).getOneData();
        article.setUser(user);
        return article;
    }

    /**
     * 搜索 ---发送至es
     * @param word
     * @param page
     * @param pageSize
     * @param sortBy
     * @param upDown
     * @return
     */
    @Override
    public ResultList selectByESKeyWord(String word, Integer page, Integer pageSize, String sortBy, String upDown) {
        try {
//            ResultList res = new ResultList();
            //Request
            SearchRequest request = new SearchRequest("article");
            //DSL
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (!StringHelperUtils.isNotEmpty(word)) {
                boolQuery.must(QueryBuilders.matchAllQuery());
            } else {
                //word添加到hotwords index ---id由ES决定,随机,不会导致重复插入丢失数据
                IndexRequest hotRequest = new IndexRequest("hotwords");
                ObjectMapper mapper = new ObjectMapper();
                //创建hotword对象
                Date datetime = new Date();
                HotwordDoc hotwordDoc = new HotwordDoc(word, datetime);
                hotRequest.source(mapper.writeValueAsString(hotwordDoc), XContentType.JSON);
                //发送请求---插入hotword
                client.index(hotRequest, RequestOptions.DEFAULT);
                boolQuery.must(QueryBuilders.multiMatchQuery(word,"artTitle","artSummary","artType"));
            }
            //条件过滤----排序
            if(StringHelperUtils.isNotEmpty(sortBy)){
                if(StringHelperUtils.isNotEmpty(upDown)&& "up".equals(upDown)){
                    request.source().query(boolQuery).sort(sortBy,SortOrder.ASC);
                }else{
                    request.source().query(boolQuery).sort(sortBy,SortOrder.DESC);
                }            }
            //组装好的查询条件放入
            request.source().query(boolQuery).sort("artPostime",SortOrder.DESC);
            //高亮
            request.source().highlighter(new HighlightBuilder()
                    .field("artTitle").field("artSummary").requireFieldMatch(false).preTags("<em style='color:red;'>").postTags("</em>"));
            //分页 from 为当前页面在总数据中的第几条数据
            request.source().from((page - 1) * pageSize).size(pageSize);
            //发送请求--全文检索查询
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析拿到res,返回
            ResultList res = handleResponse(response);
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            return res;
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * 搜索建议---发送至es
     * @param suggestKey
     * @return
     */
    @Override
    public List<String> getESSuggestWord(String suggestKey) {
        try {
            suggestKey = URLDecoder.decode(suggestKey,"UTF-8");
            //Request
            SearchRequest request = new SearchRequest("article");
            //DSL,去重,补全词条个数
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "article_search_suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(suggestKey)
                            .skipDuplicates(true)
                            .size(10)
            ));
            //发起请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析
            CompletionSuggestion suggestions = response.getSuggest().getSuggestion("article_search_suggestions");
            //取得补全词条数据
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            //遍历
            List<String> res = new ArrayList<>(options.size());
            for(CompletionSuggestion.Entry.Option option : options){
                res.add(option.getText().toString());
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 搜索文章----es by title
     * @param params
     * @return
     */
    @Override
    public ResultList getESArticleByTitleOrType(String token,RequestParamsESArt params) {
        try {
            User usr = new User();
            //check token
            if (!ObjectUtils.isEmpty(token)){
                usr = handleTokenAuthRes(token);
            }

            String title = params.getTitle();
            String type = params.getType();
            //request
            SearchRequest request = new SearchRequest("article");
            //DSL---必须匹配title
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            if(!StringHelperUtils.isNotEmpty(title)){
                boolQueryBuilder.must(QueryBuilders.matchAllQuery());
            }else{
                boolQueryBuilder.must(QueryBuilders.termQuery("artTitle.title",title));
            }
            //DSL---必须匹配type
            if(StringHelperUtils.isNotEmpty(type)){
                boolQueryBuilder.must(QueryBuilders.termQuery("artType.type",type));
            }
            //merge
            request.source().query(boolQueryBuilder);
            //res
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            ResultList res = handleResponse(response);

            HashMap<String, Object> map = new HashMap<>();
            //if size >= 1 get first
            //if have title comment
            if(StringHelperUtils.isNotEmpty(title)){
                ArticleDoc articleDoc = (ArticleDoc) res.getData().get(0);
                Integer artId = articleDoc.getArtId();
                Comment comment = new Comment();
                comment.setArtId(artId);
                ResultList resComList = comClient.selectCommentBySelectives(comment);

                if(resComList.getCode()==HttpStatusCode.HTTP_OK){
                    res.setData(resComList.getData());
                }

                //take usr and check like read follow
                if(!ObjectUtils.isEmpty(usr)){
                    Boolean exist = redisTemplate.opsForSet().isMember("likeUser:article:" + articleDoc.getArtId(), usr.getUsrId());
                    map.put("isLike",exist);
                }
                Integer read = (Integer)redisTemplate.opsForHash().get("read::article",articleDoc.getArtId());
                Long like = redisTemplate.opsForSet().size("likeUser:article:" + articleDoc.getArtId());

                //if null es not redis
                if(like != null){
                    articleDoc.setArtLike(like);
                }
                if(read != null){
                    articleDoc.setArtRead(read.longValue());
                }
                res.setOneData(articleDoc);
            }

            map.put("userInfo",usr);
            res.setMapData(map);
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据token得到usr信息获取对应id文章 es
     * @param httpRequest
     * @param params
     * @return
     */
    @Override
    public ResultList selectByTokenWithUsr(HttpServletRequest httpRequest, RequestParams params) {
        //调用usr
        try {
            ResultList usrRes = new ResultList();
            String token = httpRequest.getHeader("token");
            User usr = new User();
            //request
            SearchRequest request = new SearchRequest("article");
            //DSL
            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            if(StringHelperUtils.isNotEmpty(params.getKey())){
                //查询别人
                Integer usrId = Integer.valueOf(params.getKey());
                Object oneData = usrClient.selectByPK(token, usrId).getOneData();
                //反序列化
                usr = new ObjectMapper().convertValue(oneData, User.class);
                queryBuilder.must(QueryBuilders.termQuery("usrId",params.getKey()));
                usrRes = usrClient.getFanAndNum(token, usrId);
            }else{
                //查询自己
                usr  = handleTokenAuthRes(token);
                queryBuilder.must(QueryBuilders.termQuery("usrId",usr.getUsrId()));
                usrRes =usrClient.getFanAndNum(token,Integer.valueOf(usr.getUsrId()));
            }
            //组装好的查询条件放入
            request.source().query(queryBuilder);
            //分页 from 为当前页面在总数据中的第几条数据
            request.source().from((params.getPage() - 1) * params.getPageSize()).size(params.getPageSize());
            //发送请求--全文检索查询
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            ResultList res = handleResponse(response);
            //if query succeed
            if(HttpStatusCode.HTTP_OK == usrRes.getCode()){
                Integer fansNum = (Integer) usrRes.getOneData();
                HashMap<String, Object> map = new HashMap<>();
                map.put("fansNum", fansNum);
               //no fans jump
                if(fansNum>0){
                    map.put("fans",usrRes.getData());
                    map.put("isFollowed",usrRes.getMapData().get("isFollowed"));
                }else{
                    map.put("isFollowed",false);
                }
                res.setMapData(map);
            }
            //back res
            res.setMsg("ok");
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setOneData(usr);
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取最热列表---发送至es
     * @return
     */
    @Override
    public ResultList getHotArticleList() {
        try {
            ResultList res = new ResultList();
            //request
            SearchRequest request = new SearchRequest("hotwords");
            //DSL---设置只显示agg的数据
            request.source().size(0);
            request.source().aggregation(AggregationBuilders
                    .terms("hotWord")
                    .field("searchText")
                    .size(5)
            );
            //发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Aggregations aggregations = response.getAggregations();
            Terms titleTerms = aggregations.get("hotWord");
            List<? extends Terms.Bucket> buckets = titleTerms.getBuckets();
            ArrayList<String> titles = new ArrayList<>();
            //遍历
            for(Terms.Bucket bucket : buckets){
                String title = bucket.getKeyAsString();
                titles.add(title);
            }
            res.setData(titles);
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文章保存
     * @param article
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultList saveArticle(String token,Article article) {
        ResultList res = new ResultList();
        User usr = handleTokenAuthRes(token);
        if(Objects.isNull(usr)){
            throw new RuntimeException("token 无效");
        }
        res.setCode(HttpStatusCode.HTTP_OK);
        article.setArtPostime(new Date());
        article.setArtLike(0L);
        article.setArtRead(0L);
        article.setUsrId(usr.getUsrId());
        int inCount = articleMapper.insertSelective(article);
        if(inCount <= 0){
            res.setMsg("插入失败");
        }else{
            res.setMsg("ok");
            //发送消息----由于mybatis-mapper中设置了自增id带回,所以article会自动带回id
            rabbitTemplate.convertAndSend(MqStaticType.ARTICLE_EXCHANGE,MqStaticType.ARTICLE_INSERT_CHANGE_KEY,article);
        }
        return res;
    }

    /**
     * 文章更新
     * @param article
     * @return
     */
    @Override
    public ResultList updateArticle(Article article) {
        ResultList res = new ResultList();
        article.setArtPostime(new Date());
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        articleMapper.updateByPrimaryKeySelective(article);
        //发送消息
        rabbitTemplate.convertAndSend(MqStaticType.ARTICLE_EXCHANGE,MqStaticType.ARTICLE_INSERT_CHANGE_KEY,article);
        return res;
    }

    /**
     * 文章点赞功能 --redis 一篇文章维护一个hash likeUser::article:artId
     * @param params
     * @return
     */
    @Override
    public ResultList tapArticleLike(RequestParamsRedisArtUsr params) {
        ResultList res = new ResultList();
        //get Usr
        User usr = handleTokenAuthRes(params.getToken());
        //if/not already like
        redisTemplate.opsForSet().add("change::article",usr.getUsrId());
        Boolean exist = redisTemplate.opsForSet().isMember("likeUser:article:" + params.getArtId(), usr.getUsrId());
        if(!exist){
            redisTemplate.opsForSet().add("likeUser:article:"+ params.getArtId(), usr.getUsrId());
        }else{
            //exist del
            redisTemplate.opsForSet().remove("likeUser:article:"+ params.getArtId(), usr.getUsrId());
        }

        Long size = redisTemplate.opsForSet().size("likeUser:article:" + params.getArtId());
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setTotal(size);
        res.setOneData(!exist);
        res.setMsg("ok");
        return res;
    }

    /**
     * 文章阅读功能 --redis read::article --- key:article value:tap num
     * @param params
     * @return
     */
    @Override
    public ResultList tapArticleRead(RequestParamsRedisArtUsr params) {
        ResultList res = new ResultList();
        //if/not already read
        Boolean absent = redisTemplate.opsForHash().putIfAbsent("read::article", params.getArtId(), 1L);
        if(!absent){
             redisTemplate.opsForHash().increment("read::article", params.getArtId(), 1L);
        }
        Integer readInt = (Integer)redisTemplate.opsForHash().get("read::article", params.getArtId());
        Long read = readInt.longValue();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setOneData(read);
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList upLoadImageOrFile(MultipartFile file, HttpServletRequest request) {
        ImageUploadUtils imageUploadUtils = new ImageUploadUtils();
        ResultList res = imageUploadUtils.upLoadFileOrImages(file, request);
        return res;
    }

    @Override
    public ResultList saveVideoUrl(String token,Video params) {
        ResultList res = new ResultList();
        User user = handleTokenAuthRes(token);
        res.setCode(HttpStatusCode.HTTP_OK);
        params.setUsrId(user.getUsrId());
        params.setVideoPostime(new Date());
        params.setVideoLike(0L);
        params.setVideoPlay(0L);
        int succeed = videoMapper.insertSelective(params);
        if(succeed == 0){
            res.setMsg("fail");
            return res;
        }
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList delVideoById(Integer id) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        videoMapper.deleteByPrimaryKey(id);
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList tapToReadVideo(Integer id) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        Video video = new Video();
        video.setVideoId(id);
        video.setVideoPlay(1L);
        videoMapper.updateByPrimaryKeySelective(video);
        return res;
    }

    @Override
    public ResultList tapToLikeVideo(Integer id) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        Video video = new Video();
        video.setVideoId(id);
        video.setVideoLike(1L);
        videoMapper.updateByPrimaryKeySelective(video);
        return res;
    }

    @Override
    public ResultList selectVideoBySelective(Video record) {
        ResultList res = new ResultList();
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        List<Video> videos = videoMapper.selectBySelective(record);
        res.setData(videos);
        return res;
    }


    /**
     * 文章删除
     * @param id
     * @return
     */
    @Override
    public ResultList deleteArticle(Integer id) {
        ResultList res = new ResultList();
        articleMapper.deleteByPrimaryKey(id);
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        //发送消息
        rabbitTemplate.convertAndSend(MqStaticType.ARTICLE_EXCHANGE,MqStaticType.ARTICLE_DEL_KEY,id);
        //后续要把相关的评论都一起删除

        Comment comment = new Comment();
        comment.setArtId(id);
        comClient.delCommentBySelectives(comment);

        return res;
    }

    /**
     * mq,redis同步接口
     * @param id
     */
    @Override
    public void esRedisArtDelete(Integer id) {
        try {
            //request
            DeleteRequest request = new DeleteRequest("article", id.toString());
            //DSL
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * mq,redis同步接口
     * @param article
     */
    @Override
    public void esRedisArtInsertOrUpdate(Article article) {
        try {
            //request
            IndexRequest request = new IndexRequest("article").id(article.getArtId().toString());
            //同步更新art_title
            redisTemplate.opsForList().leftPush("newArticle",article.getArtTitle());
            redisTemplate.opsForList().trim("newArticle",0,9);
            //DEL 原有文档
            DeleteRequest requestDel = new DeleteRequest("article", article.getArtId().toString());
            //DSL
            client.delete(requestDel,RequestOptions.DEFAULT);
            //JSON
            ArticleDoc articleDoc = new ArticleDoc(article);
            ObjectMapper mapper = new ObjectMapper();
            //DSL
            request.source(mapper.writeValueAsString(articleDoc), XContentType.JSON);
            //send
            client.index(request,RequestOptions.DEFAULT);
            System.out.println("同步es,redis成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查找,通过msql
     * @param params
     * @return
     */
    @Override
    public ResultList getSelectBySelectives(Article params) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        res.setData(articleMapper.selectBySelective(params));
        return res;
    }

    @Override
    public ResultList getNewArticle() {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        List<String> mylist = redisTemplate.opsForList().range("newArticle", 0, 4);
        res.setData(mylist);
        return res;
    }


    private ResultList handleResponse(SearchResponse response) throws com.fasterxml.jackson.core.JsonProcessingException {
        ResultList res = new ResultList();
        SearchHits searchHits = response.getHits();
        //得到文档总条数
        long total = searchHits.getTotalHits().value;
        res.setTotal(total);
        SearchHit[] hits1 = searchHits.getHits();

        //遍历 反序列化
        List<ArticleDoc> data = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit h : hits1) {
            String json = h.getSourceAsString();
            ArticleDoc articleDoc = mapper.readValue(json, ArticleDoc.class);
            //获取高亮
            Map<String, HighlightField> map = h.getHighlightFields();
            if(!CollectionUtils.isEmpty(map)){
                //覆盖非高亮结果
                HighlightField titleLight = map.get("artTitle");
                HighlightField summaryLight = map.get("artSummary");
                if(!ObjectUtils.isEmpty(titleLight)){
                    String artTitle = titleLight.getFragments()[0].string();
                    articleDoc.setArtTitle(artTitle);
                }
                if(!ObjectUtils.isEmpty(summaryLight)){
                    String artSummary = summaryLight.getFragments()[0].string();
                    articleDoc.setArtSummary(artSummary);
                }
            }
            data.add(articleDoc);
        }
        res.setData(data);
        return res;
    }

    private User handleTokenAuthRes(String token){
        if(!StringHelperUtils.isNotEmpty(token)){
            return new User();
        }
        ResultList authRes = usrClient.getAuthAndCheck(token);
        try{
            if(authRes.getCode()!=HttpStatusCode.HTTP_OK){
                throw new RuntimeException("无效token,请校验");
            }
            //token有效
            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(authRes.getOneData());
            User usr = mapper.readValue(str, User.class);
            return usr;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
