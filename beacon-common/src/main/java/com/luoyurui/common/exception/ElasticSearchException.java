package com.luoyurui.common.exception;

import com.luoyurui.common.enums.ExceptionEnums;

/**
 * 搜索模块异常校验
 */
public class ElasticSearchException extends RuntimeException{

    private Integer code;

    public ElasticSearchException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public ElasticSearchException(ExceptionEnums enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }
}
