package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 号段补全:获取手机号运营商对应的归属地
 */
@Slf4j
@Service(value = "phase")
public class PhaseStrategyFilter implements StrategyFilter {
    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-号段补全校验】   校验ing…………");
    }
}
