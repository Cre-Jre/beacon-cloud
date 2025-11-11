package com.luoyurui.api.filter.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.luoyurui.api.client.BeaconCacheClient;
import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.ApiException;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 校验客户的ip是否白名单
 */
@Service(value = "ip")
@Slf4j
public class IPCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    private final String IP_ADDRESS = "ipAddress";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验ip】   校验--------");
        //1.根据CacheClient根据客户的apikey和ipAddress去查询客户的ip白名单
        String ip = cacheClient.hgetString(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), IP_ADDRESS);

        submit.setIp(ip);
        //2.判断ip白名单位null，如果ip白名单weinull，直接放行
        if(StringUtils.isEmpty(ip) || ip.contains(submit.getRealIp())){
            log.info("【接口模块-校验ip】  客户端请求IP合法！");
            return;
        }
        //3.请求的ip不在白名单内
        log.info("【接口模块-校验ip】  请求的ip不在白名单内");
        throw new ApiException(ExceptionEnums.IP_NOT_WHITE);

    }
}
