package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "black")
public class BlackStrategyFilter implements StrategyFilter {
    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-黑名单校验】   校验ing…………");

    }
}
