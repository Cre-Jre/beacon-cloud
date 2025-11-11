package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 路由策略：合适的运营商通道
 */
@Slf4j
@Service(value = "route")
public class RouteStrategyFilter implements StrategyFilter {
    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-路由策略】   校验ing…………");
    }
}
