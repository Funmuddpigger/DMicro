package mine.cloud.DMicro.mqQueueType;

public class MqStaticType {
    //article exchange
    public final static String ARTICLE_EXCHANGE ="article.topic";

    //article add change queue
    public final static String ARTICLE_INSERT_CHANGE_QUEUE ="article.insert.change.queue";

    //article del queue
    public final static String ARTICLE_DEL_QUEUE ="article.delete.queue";

    //article add/change routingKey
    public final static String ARTICLE_INSERT_CHANGE_KEY ="article.insert";

    //article del routingKey
    public final static String ARTICLE_DEL_KEY ="article.del";

    //toppic exchange
    public final static String TOPIC_EXCHANGE ="topic.topic";

    //article add change queue
    public final static String TOPIC_INSERT_CHANGE_QUEUE ="topic.insert.change.queue";

    //article del queue
    public final static String TOPIC_DEL_QUEUE ="topic.delete.queue";

    //article add/change routingKey
    public final static String TOPIC_INSERT_CHANGE_KEY ="topic.insert";

    //article del routingKey
    public final static String TOPIC_DEL_KEY ="topic.del";

}
