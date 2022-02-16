package mine.cloud.DMicro.mq;

import mine.cloud.DMicro.mqQueueType.MqStaticType;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.service.IArtServiceApi;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleMqListener {

    @Autowired
    private IArtServiceApi artServiceApi;

    /**
     * 监听article新增和修改的业务
     * @param article
     */
    @RabbitListener(queues = MqStaticType.ARTICLE_INSERT_CHANGE_QUEUE)
    public void listenArticleInsertOrUpdate(Article article){
        artServiceApi.esRedisArtInsertOrUpdate(article);
    }

    /**
     * 监听article删除的业务
     */
    @RabbitListener(queues = MqStaticType.ARTICLE_DEL_QUEUE)
    public void listenArticleDelete(Integer id){
        artServiceApi.esRedisArtDelete(id);
    }
}
