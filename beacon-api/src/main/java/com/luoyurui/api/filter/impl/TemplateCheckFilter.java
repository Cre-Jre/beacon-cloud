package com.luoyurui.api.filter.impl;

import com.luoyurui.api.client.BeaconCacheClient;
import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.constant.ApiConstant;
import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.ApiException;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * 校验客户的短信的模板
 */
@Service(value = "template")
@Slf4j
public class TemplateCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
      * 模板内容信息
     */
    private final String TEMPLATE_TEXT = "templateText";

    private final String TEMPLATE_PLACEHOLDER = "#";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验短信的模板】   校验--------");
        //- 从submit中获取到短信内容，签名信息，签名id
        String text = submit.getText();
        String sign = submit.getSign();
        Long signId = submit.getSignId();
        //- 将短信内容中的签名直接去掉，获取短信具体内容
        //- 【】去掉  从缓存中获取到签名id绑定的所有模板
        text = text.replace(ApiConstant.SIGN_PREFIX + sign +ApiConstant.SIGN_SUFFIX, "");
        //- 遍历签名绑定的所有模板信息  区缓存查询模板内容
        Set<Map> templates = cacheClient.smember(CacheConstant.CLIENT_TEMPLATE + signId);

        if(templates != null && templates.size() > 0) {
            for (Map template : templates) {
                // 4.1 将模板内容和短信具体内容做匹配-true-匹配成功
                String templateText = (String) template.get(TEMPLATE_TEXT);
                if(text.equals(templateText)){
                    // 短信具体内容和模板是匹配的。
                    log.info("【接口模块-校验模板】   校验模板通过 templateText = {}",templateText);
                    return;
                }

                //  - 判断模板中是否只包含一个变量，如果是，直接让具体短信内容匹配前缀和后缀
                //确保只有一个变量  不是两个
                if(templateText != null && templateText.contains(TEMPLATE_PLACEHOLDER)
                        && templateText.length() - templateText.replaceAll(TEMPLATE_PLACEHOLDER,"").length() == 2){
                    //确定模板不为空   并且只有一个变量
                    //获取前缀和后缀
                    String templateTextPrefix = templateText.substring(0, templateText.indexOf(TEMPLATE_PLACEHOLDER));
                    String templateTextSuffix = templateText.substring(templateText.lastIndexOf(TEMPLATE_PLACEHOLDER) + 1);
                    //判断短信具体内容是否匹配前后缀
                    if (text.startsWith(templateTextPrefix)  && text.endsWith(templateTextSuffix)) {
                        //当前短信内容匹配
                        log.info("【接口模块-校验短信的模板】   校验模板通过 TEMPLATE_TEXT={}", templateText);
                        return;
                    }
                }
            }
        }
        //- 模板校验失败
        log.info("【接口模块-校验模板】   无可用模板  text= {}", text);
        throw new ApiException(ExceptionEnums.ERROR_TEMPlATE);
    }
}
