package mine.cloud.DMicro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.feignClients.UsrClient;
import mine.cloud.DMicro.mqQueueType.MqStaticType;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.Result;
import mine.cloud.DMicro.utils.StringHelperUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArtServiceImpl implements IArtServiceApi  {

    //注入mapper
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UsrClient usrClient;

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
        //利用feign调用
        User user = usrClient.selectByPK(article.getUsrId());

        article.setUser(user);
        return article;
    }

    @Override
    public Result selectByESKeyWord(String word, Integer page, Integer pageSize,String sortBy,String upDown) {
        try {

            //Request
            SearchRequest request = new SearchRequest("article");
            //DSL
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (!StringHelperUtils.isNotEmpty(word)) {
                boolQuery.must(QueryBuilders.matchAllQuery());
            } else {
               boolQuery.must(QueryBuilders.matchQuery("searchText", word));
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
            //分页 from 为当前页面在总数据中的第几条数据
            request.source().from((page - 1) * pageSize).size(pageSize);
            //发送请求--全文检索查询
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析拿到res,返回
            Result result = handleResponse(response);
            result.setCode(HttpStatusCode.HTTP_OK);
            result.setMsg("ok");
            return result;
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

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

    @Override
    public Result saveArticle(Article article) {
        Result res = new Result();
        res.setCode(HttpStatusCode.HTTP_OK);
        article.setArtPostime(new Date());
        article.setArtLike(0L);
        article.setArtRead(0L);
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

    @Override
    public Result updateArticle(Article article) {
        Result res = new Result();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        articleMapper.updateByPrimaryKeySelective(article);
        //发送消息
        rabbitTemplate.convertAndSend(MqStaticType.ARTICLE_EXCHANGE,MqStaticType.ARTICLE_INSERT_CHANGE_KEY,article);
        return res;
    }

    @Override
    public Result deleteArticle(Integer id) {
        Result res = new Result();
        articleMapper.deleteByPrimaryKey(id);
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        //发送消息
        rabbitTemplate.convertAndSend(MqStaticType.ARTICLE_EXCHANGE,MqStaticType.ARTICLE_DEL_KEY,id);
        //后续要把相关的评论都一起删除

        return res;
    }

    @Override
    public void esArtDelete(Integer id) {
        try {
            //request
            DeleteRequest request = new DeleteRequest("article", id.toString());
            //DSL
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void esArtInsertOrUpdate(Article article) {
        try {
            //request
            IndexRequest request = new IndexRequest("article").id(article.getArtId().toString());
            //JSON
            ArticleDoc articleDoc = new ArticleDoc(article);
            ObjectMapper mapper = new ObjectMapper();
            //DSL
            request.source(mapper.writeValueAsString(articleDoc), XContentType.JSON);
            //send
            client.index(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Result handleResponse(SearchResponse response) throws com.fasterxml.jackson.core.JsonProcessingException {
        Result res = new Result();
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
            data.add(articleDoc);
        }
        res.setData(data);
        return res;
    }
}
