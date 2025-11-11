package com.luoyurui.api.util;

import com.luoyurui.api.vo.ResultVO;
import com.luoyurui.common.exception.ApiException;

public class R {

    public static ResultVO ok() {
        ResultVO r = new ResultVO();
        r.setCode(0);
        r.setMsg("接受成功");
        return r;
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO r = new ResultVO();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static ResultVO error(ApiException enums) {
        ResultVO r = new ResultVO();
        r.setCode(enums.getCode());
        r.setMsg(enums.getMessage());
        return r;
    }
}
