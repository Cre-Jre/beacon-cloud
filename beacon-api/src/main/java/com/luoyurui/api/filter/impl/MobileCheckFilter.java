package com.luoyurui.api.filter.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.luoyurui.api.filter.CheckFilter;
import com.luoyurui.api.util.PhoneFormatCheckUtil;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.ApiException;
import com.luoyurui.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 校验客户的手机号
 */
@Service(value = "mobile")
@Slf4j
public class MobileCheckFilter implements CheckFilter {

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验手机号】   校验ing…………");
        String mobile = submit.getMobile();
        if(!StringUtils.isEmpty(mobile) && PhoneFormatCheckUtil.isChinaPhone(mobile)){
            // 如果校验进来，代表手机号么得问题
            log.info("【接口模块-校验手机号】   手机号格式合法 mobile = {}",mobile);
            return;
        }
        log.info("【接口模块-校验手机号】   手机号格式不正确 mobile = {}",mobile);
        throw new ApiException(ExceptionEnums.ERROR_MOBILE);
    }

}
