package com.luoyurui.api.filter.impl;

import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 校验客户的短信的模板
 */
@Service(value = "template")
@Slf4j
public class TemplateCheckFilter implements CheckFilter {

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验短信的模板】   校验--------");
    }
}
