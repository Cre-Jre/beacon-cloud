package com.luoyurui.api.filter.impl;

import com.luoyurui.api.client.BeaconCacheClient;
import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 校验客户的apikey是否合法
 */
@Service(value = "apikey")
@Slf4j
public class ApiKeyCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验apikey】   校验--------");
        //1.基于cacheClient查询客户信息
        Map clientBusiness = cacheClient.hGetAll(CacheConstant.CLIENT_BUSINESS + submit.getApikey());
        //2.如果为null抛异常
        if (clientBusiness == null || clientBusiness.size() == 0) {
            log.info("【接口模块-校验apikey】 非法的apikey = {}" , submit.getApikey());
            throw new ApiException(ExceptionEnums.ERROR_APIKEY);
        }
        //3.正常封装数据
        submit.setClientId(Long.parseLong(clientBusiness.get("id") + ""));
//        submit.setIp(clientBusiness.get("ipAddress") + "");
        log.info("【接口模块-校验apikey】 查询到客户信息clientBusiness = {}" , clientBusiness);
    }
}
