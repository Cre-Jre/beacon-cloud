package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.StrategyException;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 全局级别
 */
@Slf4j
@Service(value = "blackGlobal")
public class BlackGlobalStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    // 黑名单的默认value
    private final String TRUE = "1";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-全局级别黑名单校验】   校验ing…………");
        //1.获取发送短信的手机号
        String mobile = submit.getMobile();
        //2.调用redis查询
        String value = cacheClient.get(CacheConstant.BLACK + mobile);
        //3.如果查询的结果为"1"，代表事黑名单
        if (TRUE.equals(value)) {
            log.info("【策略模块-全局级别黑名单校验】 当前手机号是全局黑名单  mobile = {}" + mobile);
            submit.setErrorMsg(ExceptionEnums.BLACK_GLOBAL.getMsg() + "mobile = " + mobile);
            sendMsgUtil.sendWriteLog(submit);

            //发送状态报告的消息钱，需要将report对象数据封装    发送消息到RabbitMQ
            sendMsgUtil.sendPushReport(submit);

            //一直要甩出异常
            throw new StrategyException(ExceptionEnums.BLACK_GLOBAL);
        }
        //4.不是1就结束
        log.info("【策略模块-全局级别黑名单校验】   当前手机号不是黑名单！");
    }
}
