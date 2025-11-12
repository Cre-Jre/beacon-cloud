package com.luoyurui.common.util;

import com.luoyurui.common.enums.MobileOperatorEnum;

import java.util.HashMap;
import java.util.Map;

public class OperatorUtil {

    public static Map<String, Integer> operators = new HashMap<>();

    static {
        MobileOperatorEnum[] values = MobileOperatorEnum.values();
        for (MobileOperatorEnum value : values) {
            operators.put(value.getOperatorName(), value.getOperatorId());
        }
    }

    //通过运营商名称获取对应id
    public static Integer getOperatorIdByOperatorName(String operatorName) {
        return operators.get(operatorName);
    }
}
