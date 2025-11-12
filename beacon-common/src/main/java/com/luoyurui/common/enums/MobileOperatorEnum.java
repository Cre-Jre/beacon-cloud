package com.luoyurui.common.enums;

import lombok.Getter;

@Getter
public enum MobileOperatorEnum {
    CHINA_MOLBILE(1, "移动"),
    CHINA_UNICON(2, "联通"),
    CHINA_TELECOM(3, "电信"),
    UNKOWN(0, "未知"),
    ;

    private Integer operatorId;
    private String  operatorName;



    MobileOperatorEnum(Integer operatorId, String operatorName) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }
}
