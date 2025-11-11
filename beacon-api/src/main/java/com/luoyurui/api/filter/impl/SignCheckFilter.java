package com.luoyurui.api.filter.impl;

import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 校验客户的短信的签名
 */
@Service(value = "sign")
@Slf4j
public class SignCheckFilter implements CheckFilter {

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验签名】   校验--------");
    }
}
