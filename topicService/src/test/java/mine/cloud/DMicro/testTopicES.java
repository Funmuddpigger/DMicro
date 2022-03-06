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

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class testTopicES {

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
}
