package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.constant.SmsConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.StrategyException;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 一分钟发送一条限流规则
 */
@Service("limitOneMinute")
@Slf4j
public class LimitOneMinuteStrategyFilter implements StrategyFilter {

    private final String UTC = "+8";

    private final long ONE_MINUTE = 60 * 1000 - 1;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;


    @Override
    public void strategy(StandardSubmit submit) {
        // 判断短信类型不是验证码类的，直接结束方法
        if(submit.getState() != SmsConstant.CODE_TYPE){
            return;
        }

        //1.基于submit获取短信发送时间
        LocalDateTime sendTime = submit.getSendTime();
        //2.基于localDateTime获取到时间毫秒值  然后转换
        long sendTimeMilli = sendTime.toInstant(ZoneOffset.of(UTC)).toEpochMilli();
        //3.基于submit获取客户标识和手机号信息
        Long clientId = submit.getClientId();
        String mobile = submit.getMobile();
        //4.将当前短信发送信息插入到redis的zSet结构中   zadd
        String key = CacheConstant.LIMIT_MINUTES + clientId + CacheConstant.SEPARATE + mobile;
        Boolean addOk = cacheClient.zAdd(key, sendTimeMilli, sendTimeMilli);
        //5.如果插入失败，就返回 ，有并发情况  60s不能发送两条
        if (!addOk) {
            log.info("【策略模块-一分钟限流规则】 插入失败！  满足一分钟限流规则，无法发送！");
            //封装错误信息   并发送队列
            submit.setErrorMsg(ExceptionEnums.ONE_MINUTE_LIMIT.getMsg() + "mobile = " + mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            //发送状态报告的消息钱，需要将report对象数据封装    发送消息到RabbitMQ
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_MINUTE_LIMIT);
        }
        //6.基于zra 查询1分钟直接，是否只有当前查询的发送短信信息
        long start = sendTimeMilli - ONE_MINUTE;
        int count = cacheClient.zRangeByScoreCount(key, Double.parseDouble(start + ""), Double.parseDouble(sendTimeMilli + ""));
        //7.如果大于等于两条短信信息，达到60s一条限流规则，直接返回
        if (count > 1) {
            //一分钟之前发送过短信,限流规则生效
            log.info("【策略模块-一分钟限流规则】 查询数据，  满足一分钟限流规则，无法发送！");
            cacheClient.zRemove(key,sendTimeMilli + "");
            //封装错误信息   并发送队列
            submit.setErrorMsg(ExceptionEnums.ONE_MINUTE_LIMIT.getMsg() + "mobile = " + mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            //发送状态报告的消息钱，需要将report对象数据封装    发送消息到RabbitMQ
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_MINUTE_LIMIT);
        }
        log.info("【策略模块-一分钟限流规则】 一分钟限流规则通过，可以发送！");
    }
}
