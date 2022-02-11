package mine.cloud.DMicro.config;

import mine.cloud.DMicro.mqQueueType.MqStaticType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 交换机开启持久化
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MqStaticType.ARTICLE_EXCHANGE,true,false);
    }

    @Bean
    public Queue insertChangeQueue(){
        return new Queue(MqStaticType.ARTICLE_INSERT_CHANGE_QUEUE,true);
    }

    @Bean
    public Queue delereQueue(){
        return new Queue(MqStaticType.ARTICLE_DEL_QUEUE,true);
    }
    /**
     * 绑定关系
     */
    @Bean
    public Binding insertChangeQueueBinding(){
        return BindingBuilder.bind(insertChangeQueue()).to(topicExchange()).with(MqStaticType.ARTICLE_INSERT_CHANGE_KEY);
    }

    @Bean
    public Binding deleteQueueBinding(){
        return BindingBuilder.bind(delereQueue()).to(topicExchange()).with(MqStaticType.ARTICLE_DEL_KEY);
    }

}
