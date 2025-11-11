package com.luoyurui.api.vo;

import lombok.Data;

@Data
public class ResultVO {

    /**
     * 响应数据：
     *
     *   名称   	类型     	说明
     *   code 	integer	0代表接收成功，其他code代表出错
     *   msg  	string 	例如“接收成功”，代表短信正在发送，或者是响应具体的错误信息
     *   count	integer	短信的计费条数（70个字一条，超出70个字，按照67个字一条发送）
     *   fee  	long   	扣费的金额，单位：厘 ，RMB
     *   uid  	string 	客户请求携带的uid信息
     *   sid  	long   	平台内的短信id，64位整型
     */

    private Integer code;
    private String msg;
    private Integer count;
    private Long fee;
    private String uid;
    private Long sid;
}
