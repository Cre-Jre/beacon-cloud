package com.luoyurui.api.filter.impl;

import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 校验客户的手机号
 */
@Service(value = "mobile")
@Slf4j
public class MobileCheckFilter implements CheckFilter {

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验手机号】   校验--------");
    }

}
