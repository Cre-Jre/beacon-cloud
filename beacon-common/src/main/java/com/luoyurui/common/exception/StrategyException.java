package com.luoyurui.common.exception;

import com.luoyurui.common.enums.ExceptionEnums;

/**
 * 策略模块异常校验
 */
public class StrategyException extends RuntimeException{

    private Integer code;

    public StrategyException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public StrategyException(ExceptionEnums enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }
}
