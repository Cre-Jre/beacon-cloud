package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.common.util.OperatorUtil;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.MobileOperatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 号段补全:获取手机号运营商对应的归属地
 */
@Slf4j
@Service(value = "phase")
public class PhaseStrategyFilter implements StrategyFilter {

    /**
     * 切分手机号前7位
     */
    private final int MOBILE_START = 0;
    private final int MOBILE_END = 7;
    /**
     * 校验的长度
     */
    private final int LENGTH = 2;
    /**
     * 分割区域和运营商的标识
     */
    private final String SEPARATE = ",";
    /**
     * 未知的情况
     */
    private final String UNKNOWN = "未知 未知,未知";

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private MobileOperatorUtil mobileOperatorUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-号段补全校验】   补全中");
        //1.根据手机号前七位查询手机号信息
        String mobile = submit.getMobile().substring(MOBILE_START, MOBILE_END);
        String mobileInfo = cacheClient.get(CacheConstant.PHASE + mobile);

        //3.查询不到，需要调用第三方接口查询手机号对应信息
        getMobileInfo : if (StringUtils.isEmpty(mobileInfo))  {
            mobileInfo = mobileOperatorUtil.getMobileInfoBy360(mobile);
            if (StringUtils.isNotEmpty(mobileInfo)) {
                //3、调用三方查到信息后，发送消息到MQ，并且同步到MySQL和Redis
                rabbitTemplate.convertAndSend(RabbitMQConstants.MOBILE_AREA_OPERATOR, submit.getMobile());
                break getMobileInfo;
            }
            mobileInfo = UNKNOWN;

        }
        //2.查询到之后，封装到standarSubmit对象中

        String[] areaAndOperator = mobileInfo.split(SEPARATE);
        if (areaAndOperator.length == LENGTH) {
            submit.setArea(areaAndOperator[0]);
            submit.setOperatorId(OperatorUtil.getOperatorIdByOperatorName(areaAndOperator[1]));
        }


        //4.无论是Redis还是三方接口查询到之后，封装到StandardSubmit对象中

    }
}
