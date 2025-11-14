package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.StrategyException;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.ClientBalanceUtil;
import com.luoyurui.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 扣费模块
 */
@Slf4j
@Service("fee")
public class FeeStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    private final String BALANCE = "balance";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-扣费校验】   校验ing…………");
        //1.获取submit中封装的金额
        Long fee = submit.getFee();
        Long clientId = submit.getClientId();
        //2.调用redis的decr命令去扣减金额
        Long amount = cacheClient.hIncrBy(CacheConstant.CLIENT_BALANCE + clientId, BALANCE, -fee);
        //3.获取当前客户的欠费金额的限制
        Long amountLimit = ClientBalanceUtil.getClientAmountLimit(submit.getClientId());
        //4.判断扣减过后的金额是否超出金额限制
        if (amount < amountLimit) {
            log.info("【策略模块-扣费校验】   扣除费用后，超过欠费余额的限制，无法发送短信");
            //5.如果超过，需要把扣除的费用增加回byincr，并且做后续处理
            cacheClient.hIncrBy(CacheConstant.CLIENT_BALANCE + clientId, BALANCE, fee);
            submit.setErrorMsg(ExceptionEnums.BALANCE_NOT_ENOUGH.getMsg());
            errorSendMsgUtil.sendWriteLog(submit);
            errorSendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.BALANCE_NOT_ENOUGH);
        }
        log.info("【策略模块-扣费校验】   扣费成功！！");

    }
}
