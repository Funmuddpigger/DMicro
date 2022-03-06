package mine.cloud.DMicro.config;

import mine.cloud.DMicro.mqQueueType.MqStaticType;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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
        return new TopicExchange(MqStaticType.TOPIC_EXCHANGE,true,false);
    }

    @Bean
    public Queue insertChangeQueue(){
        return new Queue(MqStaticType.TOPIC_INSERT_CHANGE_QUEUE,true);
    }

    @Bean
    public Queue delereQueue(){
        return new Queue(MqStaticType.TOPIC_DEL_QUEUE,true);
    }
    /**
     * 绑定关系
     */
    @Bean
    public Binding insertChangeQueueBinding(){
        return BindingBuilder.bind(insertChangeQueue()).to(topicExchange()).with(MqStaticType.TOPIC_INSERT_CHANGE_KEY);
    }

    @Bean
    public Binding deleteQueueBinding(){
        return BindingBuilder.bind(delereQueue()).to(topicExchange()).with(MqStaticType.TOPIC_DEL_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

}
