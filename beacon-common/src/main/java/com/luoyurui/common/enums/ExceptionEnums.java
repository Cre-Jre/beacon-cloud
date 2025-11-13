package com.luoyurui.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnums {
    ERROR_APIKEY(-1, "非法的apikey"),
    IP_NOT_WHITE(-2, "请求的ip不在白名单内"),
    ERROR_SIGN(-3, "无可用签名"),
    ERROR_TEMPlATE(-4, "无可用模板"),
    ERROR_MOBILE(-5, "手机号格式不正确"),
    BALANCE_NOT_ENOUGH(-6, "客户余额不足"),
    PARAMETER_ERROR(-10,"参数不合法！"),
    SNOWFLAKE_OUT_OF_RANGE(-11,"雪花算法的机器id或服务id超出最大范围！"),
    SNOWFLAKE_TIME_BACK(-12,"雪花算法的服务器出现时间回拨问题！"),
    HAVE_DIRTY_WORD(-13,"当前短信内容敏感词校验未通过！"),
    BLACK_GLOBAL(-14,"当前手机号为平台黑名单！"),
    BLACK_CLIENT(-15,"当前手机号为客户黑名单！"),
    ONE_MINUTE_LIMIT(-16,"一分钟限流规则生效，无法发送短信！"),
    ONE_HOUR_LIMIT(-17,"一小时限流规则生效，无法发送短信！"),
    ;

    private Integer code;
    private String msg;

    ExceptionEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
