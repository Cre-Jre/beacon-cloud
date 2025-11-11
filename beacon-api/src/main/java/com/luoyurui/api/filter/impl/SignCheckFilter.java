package com.luoyurui.api.filter.impl;

import com.luoyurui.api.client.BeaconCacheClient;
import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.common.constant.ApiConstant;
import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.ApiException;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * 校验客户的短信的签名
 */
@Service(value = "sign")
@Slf4j
public class SignCheckFilter implements CheckFilter {

    private final int SIGN_START_INDEX = 1;

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * 客户存储签名信息的字段
     */
    private final String CLIENT_SIGN_INFO = "signInfo";

    /**
     * 签名的id
     */
    private final String SIGN_ID = "id";


    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验签名】   校验--------");
        //1.判断短信内容是否携带【】
        String text = submit.getText();
        if (!text.startsWith(ApiConstant.SIGN_PREFIX) || !text.contains(ApiConstant.SIGN_SUFFIX)){
            log.info("【接口模块-校验签名】   无可用签名  text= {}", text);
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }
        //2.将短信内容中的签名截取出来
        String sign = text.substring(SIGN_START_INDEX, text.indexOf(ApiConstant.SIGN_SUFFIX));
        if (StringUtils.isEmpty(sign)) {
            log.info("【接口模块-校验签名】   无可用签名  text= {}", text);
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }
        //3.从缓存中查询出客户绑定的签名
        Set<Map> setM = cacheClient.smember(CacheConstant.CLIENT_SIGN + submit.getClientId());
        if (setM == null || setM.size() == 0) {
            log.info("【接口模块-校验签名】   无可用签名  text= {}", text);
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }
        //4.判断
        for (Map map : setM) {
            if (sign.equals(map.get(CLIENT_SIGN_INFO))) {
                submit.setSign(sign);
                submit.setSignId(Long.parseLong(map.get(SIGN_ID) + ""));
                log.info("【接口模块-校验签名】   找到匹配的签名  sign= {}", sign);
                return;
            }
        }

        //5. 没有匹配的签名
        log.info("【接口模块-校验签名】   无可用签名 text = {}",text);
        throw new ApiException(ExceptionEnums.ERROR_SIGN);
    }
}
