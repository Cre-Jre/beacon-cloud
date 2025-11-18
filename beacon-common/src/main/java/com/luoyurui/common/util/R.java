package com.luoyurui.common.util;

import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.vo.ResultVO;

/**
 * 封装resultVo工具
 */

public class R {

    /**
     * 成功
     * @return
     */
    public static ResultVO ok() {
        return new ResultVO(0, "");
    }

    /**
     * 失败，返回错误数据
     * @param enums
     * @return
     */
    public static ResultVO error(ExceptionEnums enums) {
        return new ResultVO(enums.getCode(), enums.getMsg());
    }
}
