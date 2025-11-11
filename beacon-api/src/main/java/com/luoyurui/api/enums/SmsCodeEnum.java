package com.luoyurui.api.enums;

import lombok.Getter;

/**
 * 相应信息枚举类
 */
@Getter
public enum SmsCodeEnum {
    PARAMETER_ERROR(-10,"参数不合法！");

    private Integer code;
    private String msg;

    SmsCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
