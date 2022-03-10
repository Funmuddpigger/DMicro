package mine.cloud.DMicro;

import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.TopicMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.doc.TopicDoc;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.Topic;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class testTopicES {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testBulkAddDocument() throws IOException {
        Topic topic = new Topic();
        //根据ID查询数据
        List<Topic> tops = topicMapper.selectBySelectives(topic);
        //准备Request对象
        BulkRequest bulkRequest = new BulkRequest();
        ObjectMapper mapper = new ObjectMapper();
        for(Topic a : tops){
            //转换为es索引库对应的实体类
            TopicDoc topicDoc = new TopicDoc(a);
            //转json
            bulkRequest.add(new IndexRequest("topic")
                    .id(topicDoc.getTopicId().toString())
                    .source(mapper.writeValueAsString(topicDoc), XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Test
    void testaddRedis(){
//        redisTemplate.opsForZSet().add("topic:quote", 2, 20L);
//        redisTemplate.opsForZSet().add("topic:quote", 3, 40L);
//        redisTemplate.opsForZSet().add("topic:quote", 4, 30L);
//        redisTemplate.opsForZSet().add("topic:quote", 5, 10L);
        redisTemplate.opsForZSet().incrementScore("topic:quote",2,1);
    }

    @Test
    void testRedis(){
        Set set = redisTemplate.opsForZSet().reverseRange("topic:quote", 0, 4);
        ArrayList<Integer> topics = new ArrayList<>();

    }
}
