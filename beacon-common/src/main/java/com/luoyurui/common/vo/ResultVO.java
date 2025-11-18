package com.luoyurui.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 相应前端护具的基本结构
 */

@Data
@NoArgsConstructor
public class ResultVO {

    private Integer code;
    private String msg;

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
