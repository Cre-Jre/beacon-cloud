package com.luoyurui.search.mq;

import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.model.StandardReport;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 接收修改
 */
@Slf4j
@Component
public class SmsUpdateLogListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(queues = RabbitMQConstants.SMS_GATEWAY_DEAD_QUEUE)
    public void consume(StandardReport report, Channel channel, Message message) throws IOException {
        log.info("【搜素模块-修改日志】 接收到修改日志的消息 report = {}", report);
        //将report对象存如reportThreadLocal中，方便在搜索模块获取
        SearchUtils.set(report);
        //调用搜索模块完成的修改操作
        Map<String, Object> doc = new HashMap<>();
        doc.put("reportState", report.getReportState());
        searchService.update(SearchUtils.INDEX + SearchUtils.getYear(),report.getSequenceId().toString(),doc);

        //ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
