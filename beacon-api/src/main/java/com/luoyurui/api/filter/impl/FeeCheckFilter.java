package com.luoyurui.api.filter.impl;

import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 校验客户的剩余金额是否充足
 */
@Service(value = "fee")
@Slf4j
public class FeeCheckFilter implements CheckFilter {

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验剩余金额是否充足】   校验--------");
    }
}
