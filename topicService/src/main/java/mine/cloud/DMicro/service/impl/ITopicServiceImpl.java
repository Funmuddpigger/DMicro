package mine.cloud.DMicro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.TopUsrMapper;
import mine.cloud.DMicro.dao.TopicMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.doc.TopicDoc;
import mine.cloud.DMicro.feignClients.UsrClient;
import mine.cloud.DMicro.mqQueueType.MqStaticType;
import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.TopUsr;
import mine.cloud.DMicro.pojo.Topic;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.ITopUsrService;
import mine.cloud.DMicro.service.ITopicService;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.ResultList;
import mine.cloud.DMicro.utils.StringHelperUtils;
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
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Service
public class ITopicServiceImpl implements ITopicService, ITopUsrService {

    @Autowired
    private UsrClient usrClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TopUsrMapper topUsrMapper;

    @Autowired
    private RestHighLevelClient client;

    /**
     * Search topic -sql
     * @param token
     * @param record
     * @return
     */
    @Override
    public ResultList selectTopicBySelectives(String token, Topic record) {
        ResultList res = new ResultList();
        res.setData(topicMapper.selectBySelectives(record));
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        return res;
    }

    /**
     * add topic -sql
     * @param token
     * @param record
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ResultList addTopicBySelective(String token, Topic record) {
        ResultList res = new ResultList();
        User usr = handleTokenAuthRes(token);
        if(Objects.isNull(usr)){
            throw new RuntimeException("token 无效");
        }
        record.setTopicQuote(0L);
        record.setTopicCreateTime(new Date());
        record.setTopicCreateUsr(usr.getUsrId());
        int success = topicMapper.insertSelective(record);
        if(success <=0){
            res.setMsg("插入话题失败");
            res.setCode(HttpStatusCode.HTTP_DATA_BAD);
        }else{
            res.setMsg("ok");
            res.setCode(HttpStatusCode.HTTP_OK);
            //send msg MQ to ES  主键回带
            rabbitTemplate.convertAndSend(MqStaticType.TOPIC_EXCHANGE,MqStaticType.TOPIC_INSERT_CHANGE_KEY,record);
            res.setOneData(record);
        }
        return res;
    }

    /**
     * Search --es
     * @param params
     * @return
     */
    @Override
    public ResultList selectByESKeyWord(RequestParams params) {
        try {
            //request
            SearchRequest request = new SearchRequest("topic");
            //DSL
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if(!StringHelperUtils.isNotEmpty(params.getKey())){
                //search all
                boolQuery.must(QueryBuilders.matchAllQuery());
            }else{
                boolQuery.must(QueryBuilders.matchQuery("topicText",params.getKey()));
            }
            //merge
            request.source().query(boolQuery);
            request.source().from((params.getPage()-1)*params.getPageSize()).size(params.getPageSize());
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            ResultList res = handleResponse(response);
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultList getESSuggestWord(String suggestKey) {
        ResultList res = new ResultList();
        try {
            URLDecoder.decode(suggestKey,"UTF-8");
            //Request
            SearchRequest request = new SearchRequest("topic");
            //DEL,去重
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "topic_search_suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(suggestKey).skipDuplicates(true)
                            .size(10)
            ));
            //send
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            CompletionSuggestion suggestions = response.getSuggest().getSuggestion("topic_search_suggestions");
            //get data
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            ArrayList<HashMap> lists = new ArrayList<>();
            for(CompletionSuggestion.Entry.Option option : options){
                HashMap<String, Object> map = new HashMap<>();
                map.put("id",option.getHit().getId());
                map.put("value",option.getText().toString());
                lists.add(map);
            }
            res.setData(lists);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ResultList addTopicViewWithUsr(String token, TopUsr record) {
        ResultList res = new ResultList();
        User user = handleTokenAuthRes(token);
        // topic id is null ,insert
        if(Objects.isNull(record.getTopicId())){
            Topic topic = new Topic();
            topic.setTopicText(record.getTopicText());
            res = addTopicBySelective(token, record);
            topic = (Topic) res.getOneData();
            //初始化redis topic排行
            redisTemplate.opsForZSet().add("topic:quote",record.getTopicId(),0);
            record.setUsrId(user.getUsrId());
            record.setTopicUsrPostTime(new Date());
        }else{
            //topic exist
            record.setUsrId(user.getUsrId());
            record.setTopicUsrPostTime(new Date());
        }

        //insert topicUsr
        int success = topUsrMapper.insertSelective(record);
        if(success <=0){
            res.setMsg("发布失败");
            res.setCode(HttpStatusCode.HTTP_DATA_BAD);
        }else{
            //引用次数加一
            redisTemplate.opsForZSet().incrementScore("topic:quote",record.getTopicId(),1);
            res.setMsg("ok");
            res.setCode(HttpStatusCode.HTTP_OK);
        }
        return res;
    }

    @Override
    public ResultList delTopicViewWithUsr(String token, Integer id) {
        topUsrMapper.deleteByPrimaryKey(id);
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        return res;
    }

    @Override
    public void batchInsertToES() {

    }

    //添加时修改es,异步加入
    @Override
    public void esTopicInsertOrUpdate(Topic topic) {
        //request
        IndexRequest request = new IndexRequest("topic").id(topic.getTopicId().toString());
        TopicDoc topicDoc = new TopicDoc(topic);
        //JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            request.source(mapper.writeValueAsString(topicDoc), XContentType.JSON);
            //send
            client.index(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void esTopicDelete(Integer id) {
        //TODO del
    }

    @Override
    public ResultList getTopTopic(Integer last) {
        ResultList res = new ResultList();
        Set<Integer> rank = redisTemplate.opsForZSet().reverseRange("topic:quote", 0, last-1);
        ArrayList<Topic> topics = new ArrayList<>();
        for(Integer i : rank){
            Topic topic = topicMapper.selectByPrimaryKey(i);
            topics.add(topic);
        }
        res.setData(topics);
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        return res;
    }

    @Override
    public ResultList getTopicBySelective(String token, TopUsr record) {
        ResultList res = new ResultList();
        List<TopUsr> topUsrs = topUsrMapper.selectBySelectives(record);

        ArrayList<HashMap> maps = new ArrayList<>();
        for(TopUsr topUsr : topUsrs){
            HashMap<String, Object> map = new HashMap<>();
            User user = usrClient.selectByPK(token, topUsr.getUsrId());
            Topic topic = topicMapper.selectByPrimaryKey(topUsr.getTopicId());
            map.put("topicUsr",topUsr);
            map.put("usrInfo",user);
            map.put("topic",topic);
            maps.add(map);
        }
        res.setData(maps);
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
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
        List<TopicDoc> data = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit h : hits1) {
            String json = h.getSourceAsString();
            TopicDoc topicDoc = mapper.readValue(json, TopicDoc.class);
            data.add(topicDoc);
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
