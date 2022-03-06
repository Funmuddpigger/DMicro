package mine.cloud.DMicro.mq;

import mine.cloud.DMicro.mqQueueType.MqStaticType;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.Topic;
import mine.cloud.DMicro.service.ITopicService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleMqListener {

    @Autowired
    private ITopicService iTopicService;

    /**
     * 监听article新增和修改的业务
     * @param topic
     */
    @RabbitListener(queues = MqStaticType.TOPIC_INSERT_CHANGE_QUEUE)
    public void listenArticleInsertOrUpdate(Topic topic){
        iTopicService.esTopicInsertOrUpdate(topic);
    }

    /**
     * 监听article删除的业务
     */
    @RabbitListener(queues = MqStaticType.TOPIC_DEL_QUEUE)
    public void listenArticleDelete(Integer id){
        iTopicService.esTopicDelete(id);
    }
}
