package com.luoyurui.strategy.util;

public class ClientBalanceUtil {

    /**
     * 后期如果要给客户只当欠费额度等级，在重写此方法
     * @param clientId
     * @return
     */
    public static Long getClientAmountLimit(Long clientId) {
        return -10000L;
    }
}
