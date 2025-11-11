package com.luoyurui.strategy.mq;

import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.filter.StrategyFilterContext;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PreSendListener {

    @Autowired
    private StrategyFilterContext strategyFilterContext;

    @RabbitListener(queues = RabbitMQConstants.SMS_PRE_SEND)
    public void listener(StandardSubmit submit, Message message, Channel channel) throws IOException {
        log.info("【策略模块-接收消息】 接收到接口模块发送的消息 submit = {}",submit);

        try {
            strategyFilterContext.strategy(submit);
            log.info("【策略模块-消费完毕】手动ack");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【策略模块-失败】");
        }

    }
}
