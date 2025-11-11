package com.luoyurui.api.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SingleSendForm {

    /**
     * 请求参数：
     *
     *   参数名   	类型     	是否必传	说明                     	示例
     *   apikey	string 	是   	由服务方提供，可以在后台首页中查看      	887559db54d911edba520242ac120002
     *   mobile	string 	是   	接收的手机号，仅支持单号码发送        	18888888888
     *   text  	string 	是   	需要发送的短信内容，需要与签名和模板匹配   	【烽火云】 您的验证码是 1234
     *   uid   	string 	否   	您业务系统内的ID，回调时会携带此参数    	10086
     *   state 	integer	是   	0-验证码短信 1-通知类短信 2-营销类短信	0
     */
    @NotBlank(message = "apikey不允许为空")
    private String apikey;
    @NotBlank(message = "手机号不允许为空")
    private String mobile;
    @NotBlank(message = "短信内容不允许为空")
    private String text;
    private String uid;
    @Range(min = 0, max = 2, message = "短信类型只能是0~2的整数!")
    @NotNull(message = "短信类型不允许为空")
    private Integer state;
}
