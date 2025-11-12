package com.luoyurui.strategy.filter.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 携号转网策略
 */
@Slf4j
@Service(value = "transfer")
public class TransferStrategyFilter implements StrategyFilter {

    // 携号转网标志
    private final Boolean TRANSFER = true;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-携号转网策略】   校验ing…………");

        //1、获取用户手机号
        String mobile = submit.getMobile();

        //2、直接基于Redis查询携号转网信息
        String value = cacheClient.get(CacheConstant.TRANSFER + mobile);

        //3、如果存在携号转网，设置运营商信息
        if(!StringUtils.isEmpty(value)){
            // 代表携号转网了
            submit.setOperatorId(Integer.valueOf(value));
            submit.setIsTransfer(TRANSFER);
            log.info("【策略模块-携号转网策略】   当前手机号携号转网");
            return;
        }

        log.info("【策略模块-携号转网策略】   携号转网策略结束");
    }
}
