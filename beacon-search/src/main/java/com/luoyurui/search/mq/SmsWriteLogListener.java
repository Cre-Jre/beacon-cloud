package com.luoyurui.search.mq;

import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.common.util.JsonUtil;
import com.luoyurui.search.service.SearchService;
import com.luoyurui.search.utils.SearchUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 写日志的消费者
 */
@Slf4j
@Component
public class SmsWriteLogListener {

    @Autowired
    private SearchService searchService;


    @RabbitListener(queues = RabbitMQConstants.SMS_WRITE_LOG)
    public void consume(StandardSubmit submit, Channel channel, Message message) throws IOException {
        //1.调用搜索模块的添加方法，完成添加操作
        log.info("接收到存储日志的信息   submit = {}",submit);
        searchService.index(SearchUtils.INDEX + SearchUtils.getYear(),submit.getSequenceId().toString(), JsonUtil.obj2JSON(submit));
        //2.手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


}