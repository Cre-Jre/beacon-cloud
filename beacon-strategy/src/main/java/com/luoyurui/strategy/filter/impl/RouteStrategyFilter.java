package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.StrategyException;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.ChannelTransferUtil;
import com.luoyurui.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 路由策略：合适的运营商通道
 */
@Slf4j
@Service(value = "route")
public class RouteStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-路由策略】   校验ing…………");

        //1.拿到客户id
        Long clientId = submit.getClientId();
        //2.基于redis获取当前客户绑定所有通道信息
        Set<Map> clientChannels = cacheClient.smemberMap(CacheConstant.CLIENT_CHANNEL + clientId);
        //3.将获取到的客户通道信息根据权重做好排序
        TreeSet<Map> clientWeightChannels = new TreeSet<>(new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int o1Weight = Integer.parseInt(o1.get("clientChannelWeight") + "");
                int o2Weight = Integer.parseInt(o2.get("clientChannelWeight") + "");
                return o2Weight - o1Weight;
            }
        });
        clientWeightChannels.addAll(clientChannels);

        boolean ok = false;
        Map channel = null;
        Map clientChannel = null;
        //4.基于排序的通道选择，权重更高的
        for (Map clientWeightChannel : clientWeightChannels) {
            //5.如果通道可用，基于redis查询具体通道信息
            if((int)(clientWeightChannel.get("isAvailable")) != 0){
                // 当前关系不可用，直接进行下次循环，选择权重相对更低一点的
                continue;
            }
            //6.如果通道信息查询后，依然可用，运营商去匹配
            channel = cacheClient.hGetAll(CacheConstant.CHANNEL + clientWeightChannel.get("channelId"));
            if((int)(channel.get("isAvailable")) != 0){
                // 当前通道不可用，选择权重更低的通道~
                continue;
            }
            // 获取通道的通讯方式
            Integer channelType = (Integer) channel.get("channelType");
            if (channelType != 0 && submit.getOperatorId() != channelType){
                // 通道不是全网通，并且和当前手机号运营商不匹配
                continue;
            }

            //7.如果后期涉及到通道   TODO
            Map transferChannel = ChannelTransferUtil.transfer(submit, channel);
            // 找到可以使用的通道了
            ok = true;
            break;
        }

        if(!ok){
            log.info("【策略模块-路由策略】   没有选择到可用的通道！！");
            submit.setErrorMsg(ExceptionEnums.NO_CHANNEL.getMsg());
            sendMsgUtil.sendWriteLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.NO_CHANNEL);
        }

        //8、基于选择的通道封装submit的信息
        submit.setChannelId(Long.parseLong(channel.get("id") + ""));
        submit.setSrcNumber("" + channel.get("channelNumber") + clientChannel.get("clientChannelNumber"));

        try {
            //9、声明好队列名称，并构建队列
            String queueName = RabbitMQConstants.SMS_GATEWAY + submit.getChannelId();
            amqpAdmin.declareQueue(QueueBuilder.durable(queueName).build());

            //10、发送消息到声明好的队列中
            rabbitTemplate.convertAndSend(queueName,submit);
        } catch (AmqpException e) {
            log.info("【策略模块-路由策略】   声明通道对应队列以及发送消息时出现了问题！");
            submit.setErrorMsg(e.getMessage());
            sendMsgUtil.sendWriteLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(e.getMessage(),ExceptionEnums.UNKNOWN_ERROR.getCode());
        }
    }
}
