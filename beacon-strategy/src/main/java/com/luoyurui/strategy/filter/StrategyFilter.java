package com.luoyurui.strategy.filter;

import com.luoyurui.common.model.StandardSubmit;

public interface StrategyFilter {

    /**
     * 校验！！！！
     * @param submit
     */
    void strategy(StandardSubmit submit);
}
