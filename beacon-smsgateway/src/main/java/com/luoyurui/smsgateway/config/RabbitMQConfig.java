package com.luoyurui.smsgateway.config;

import com.luoyurui.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 针对配置采用
 */
@Configuration
public class RabbitMQConfig {

    private final int TTL = 10000;

    private final String FANOUT_ROUTING_KEY = "";

    //使用死信队列   交换机没办法确保消息不会丢
    //需要普通交换机和普通队列    死信交换机和死信队列
    @Bean
    public Exchange normalExchange() {
        return ExchangeBuilder.fanoutExchange(RabbitMQConstants.SMS_GATEWAY_NORMAL_EXCHANGE).build();
    }

    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable(RabbitMQConstants.SMS_GATEWAY_NORMAL_QUEUE)
                .withArgument("x-message-ttl", TTL)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.SMS_GATEWAY_DEAD_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", FANOUT_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding normalBinding(Exchange normalExchange, Queue normalQueue) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("").noargs();
    }

    @Bean
    public Exchange deadExchange() {
        return ExchangeBuilder.fanoutExchange(RabbitMQConstants.SMS_GATEWAY_DEAD_EXCHANGE).build();
    }

    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(RabbitMQConstants.SMS_GATEWAY_DEAD_QUEUE).build();
    }

    @Bean
    public Binding deadBinding(Exchange deadExchange, Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("").noargs();
    }


//配置类的方式修改rabbitMq消费方式
//    @Bean
    public SimpleRabbitListenerContainerFactory gatewayContainerFactory(ConnectionFactory connectionFactory,
                                                                        SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(5);
        simpleRabbitListenerContainerFactory.setPrefetchCount(10);
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(simpleRabbitListenerContainerFactory, connectionFactory);
        return simpleRabbitListenerContainerFactory;
    }
}
