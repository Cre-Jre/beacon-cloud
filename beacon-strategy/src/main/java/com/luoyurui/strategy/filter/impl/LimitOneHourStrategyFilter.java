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

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 一小时发送三条限流规则
 */
@Service("limitOneHour")
@Slf4j
public class LimitOneHourStrategyFilter implements StrategyFilter {

    private final String UTC = "+8";

    private final long ONE_HOUR = 60 * 1000 * 60 - 1;

    private final int RETRY_COUNT = 2;

    private final int LIMIT_HOUR = 3;

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
        submit.setOneHourLimitMilli(sendTimeMilli);
        //3.基于submit获取客户标识和手机号信息
        Long clientId = submit.getClientId();
        String mobile = submit.getMobile();
        //4.将当前短信发送信息插入到redis的zSet结构中   zadd
        String key = CacheConstant.LIMIT_HOURS + clientId + CacheConstant.SEPARATE + mobile;

        //5.如果插入失败，需要重新将毫秒值做改变，然后重新插入
        int retry = 0;
        while (!cacheClient.zAdd(key, submit.getOneHourLimitMilli(), submit.getOneHourLimitMilli())) {
            //发送失败  尝试重试
            if (retry == RETRY_COUNT) {
                break;
            }
            retry ++;
            //插入失败是因为存的member不允许重复，如果重复了，需要把时间向后移动
            submit.setOneHourLimitMilli(System.currentTimeMillis());
        }
        //如果retry为2，代表已经重试两次但是还是没有成功
        if (retry == RETRY_COUNT) {
            //一分钟之前发送过短信,限流规则生效
            log.info("【策略模块-一小时限流规则】 查询数据，  满足一小时限流规则，无法发送！");
            //封装错误信息   并发送队列
            submit.setErrorMsg(ExceptionEnums.ONE_HOUR_LIMIT.getMsg() + "mobile = " + mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            //发送状态报告的消息钱，需要将report对象数据封装    发送消息到RabbitMQ
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_HOUR_LIMIT);
        }

        // 没有重试2次，3次之内，将数据正常的插入了。基于zrangebyscore做范围查询
        long start = submit.getOneHourLimitMilli() - ONE_HOUR;
        int count = cacheClient.zRangeByScoreCount(key, Double.parseDouble(start + ""), Double.parseDouble(submit.getOneHourLimitMilli() + ""));

        if(count > LIMIT_HOUR){
            log.info("【策略模块-一小时限流策略】  插入失败！ 满足一小时限流规则，无法发送！");
            cacheClient.zRemove(key,submit.getOneHourLimitMilli() + "");
            submit.setErrorMsg(ExceptionEnums.ONE_HOUR_LIMIT + ",mobile = " + mobile);
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_HOUR_LIMIT);
        }

        log.info("【策略模块-一小时限流策略】  一小时限流规则通过，可以发送！");
    }
}
