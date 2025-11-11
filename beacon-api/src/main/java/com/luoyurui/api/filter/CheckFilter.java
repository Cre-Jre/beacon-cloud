package com.luoyurui.api.filter;

import com.luoyurui.common.model.StandardSubmit;

/**
 * 策略模式父接口
 */
public interface CheckFilter {

    /**
     * 接参数在校验
     */
    void check(StandardSubmit submit);
}
