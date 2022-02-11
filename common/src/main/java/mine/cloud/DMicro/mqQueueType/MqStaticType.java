package mine.cloud.DMicro.mqQueueType;

public class MqStaticType {
    //article exchange
    public final static String ARTICLE_EXCHANGE ="hotel.topic";

    //article add change queue
    public final static String ARTICLE_INSERT_CHANGE_QUEUE ="hotel.insert.change.queue";

    //article del queue
    public final static String ARTICLE_DEL_QUEUE ="hotel.delete.queue";

    //article add/change routingKey
    public final static String ARTICLE_INSERT_CHANGE_KEY ="hotel.insert";

    //article del routingKey
    public final static String ARTICLE_DEL_KEY ="hotel.del";

}
