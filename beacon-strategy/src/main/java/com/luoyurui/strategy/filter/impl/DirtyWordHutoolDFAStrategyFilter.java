package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.constant.SmsConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.StrategyException;
import com.luoyurui.common.model.StandardReport;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.ErrorSendMsgUtil;
import com.luoyurui.strategy.util.HutoolDFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "hutoolDFADirtyWord")
@Slf4j
public class DirtyWordHutoolDFAStrategyFilter implements StrategyFilter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】   校验ing…………");
        //1、 获取短信内容
        String text = submit.getText();

        //2、 调用DFA查看敏感词
        List<String> dirtyWords = HutoolDFAUtil.getDirtyWord(text);

        //4、 根据返回的set集合，判断是否包含敏感词
        if (dirtyWords != null && dirtyWords.size() > 0) {
            //5、 如果有敏感词，抛出异常 / 其他操作。。
            log.info("【策略模块-敏感词校验】   短信内容包含敏感词信息， dirtyWords = {}", dirtyWords);
            //封装错误信息   并发送队列
            errorSendMsgUtil.sendWriteLog(submit, dirtyWords);

            //发送状态报告的消息钱，需要将report对象数据封装    发送消息到RabbitMQ
            errorSendMsgUtil.sendPushReport(submit);

            throw new StrategyException(ExceptionEnums.HAVE_DIRTY_WORD);

        }

        log.info("【策略模块-敏感词校验】   校验通过，没有敏感词信息");
    }
}