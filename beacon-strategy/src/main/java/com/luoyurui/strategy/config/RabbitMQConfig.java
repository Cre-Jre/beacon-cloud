package com.luoyurui.strategy.config;

import com.luoyurui.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 构建队列  交换机信息
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 策略模块发送手机号归属地，运营商到后台管理模块
     * @return
     */
    @Bean
    public Queue preSendQueue() {
        return QueueBuilder.durable(RabbitMQConstants.MOBILE_AREA_OPERATOR).build();
    }

    /**
     * 写日志的队列。
     * @return
     */
    @Bean
    public Queue writeLogQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_WRITE_LOG).build();
    }

    /**
     * 状态报告队列。
     * @return
     */
    @Bean
    public Queue pushReportQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_PUSH_REPORT).build();
    }
}
