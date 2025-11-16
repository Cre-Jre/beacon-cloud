package com.luoyurui.smsgateway.runable;

import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.constant.SmsConstant;
import com.luoyurui.common.model.StandardReport;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.common.util.CMPP2ResultUtil;
import com.luoyurui.common.util.CMPPDeliverMapUtil;
import com.luoyurui.common.util.CMPPSubmitRepoMapUtil;
import com.luoyurui.smsgateway.netty4.entity.CmppSubmitResp;
import com.luoyurui.smsgateway.util.SpringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;

public class SubmitRepoRunnable implements Runnable {

    private CmppSubmitResp submitResp;

    public SubmitRepoRunnable(CmppSubmitResp submitResp) {
        this.submitResp = submitResp;
    }

    private final int OK = 0;

    private RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClass(RabbitTemplate.class);

    @Override
    public void run() {
        //1.拿到自增id，从concurrentHashMap中获取存储的submit
        StandardSubmit submit = CMPPSubmitRepoMapUtil.remove(submitResp.getSequenceId());
        //2.根据运营商返回的result封装，确认短信状态并封装submit
        int result = submitResp.getResult();
        StandardReport report = null;
        if (result != OK) {
            //说明运营商提交应答中回馈了失败的情况
            String resultMessage = CMPP2ResultUtil.getResultMessage(result);
            submit.setReportState(SmsConstant.REPORT_FAIL);
            submit.setErrorMsg(resultMessage);
        } else {
            //如果没进入if 说明正确接收发送短信任务
            //3.将submit封装为report，临时存储，一遍运营商返回状态码时，可以再次回去信息
            report = new StandardReport();
            BeanUtils.copyProperties(submit, report);
            CMPPDeliverMapUtil.put(submitResp.getMsgId() + "",report);
        }
        //将封装好的submit直接丢到rabbitMq中，让搜哦模块记录信息
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG, submit);

    }
}
