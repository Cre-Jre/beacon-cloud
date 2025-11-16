package com.luoyurui.smsgateway.netty4;


import com.luoyurui.common.constant.SmsConstant;
import com.luoyurui.common.model.StandardReport;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.common.util.CMPP2ResultUtil;
import com.luoyurui.common.util.CMPPDeliverMapUtil;
import com.luoyurui.common.util.CMPPSubmitRepoMapUtil;
import com.luoyurui.smsgateway.netty4.entity.CmppDeliver;
import com.luoyurui.smsgateway.netty4.entity.CmppSubmitResp;
import com.luoyurui.smsgateway.netty4.utils.MsgUtils;
import com.luoyurui.smsgateway.runable.DeliverRunnable;
import com.luoyurui.smsgateway.runable.SubmitRepoRunnable;
import com.luoyurui.smsgateway.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * 主要业务 handler,运营商响应信息
 */
@Slf4j
public class CMPPHandler extends SimpleChannelInboundHandler {


    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof CmppSubmitResp){
            System.out.println(Thread.currentThread());
            Thread.sleep(1000);
            CmppSubmitResp resp=(CmppSubmitResp)msg;
            log.info("-------------接收到短信提交应答-------------");
            log.info("----自增id："+resp.getSequenceId());
            log.info("----状态："+ resp.getResult());
            log.info("----第一次响应："+resp.getMsgId());
            ThreadPoolExecutor cmppSubmitPool = (ThreadPoolExecutor) SpringUtil.getBeanByName("cmppSubmitPool");
            //4.将封装好信息放入线程池中，
            cmppSubmitPool.execute(new SubmitRepoRunnable(resp));
        }

        if (msg instanceof CmppDeliver){
            CmppDeliver resp=(CmppDeliver)msg;
            // 是否为状态报告 0：非状态报告1：状态报告
            if (resp.getRegistered_Delivery() == 1) {
                // 如果是状态报告的话
                log.info("-------------状态报告---------------");
                log.info("----第二次响应："+resp.getMsg_Id_DELIVRD());
                log.info("----手机号："+resp.getDest_terminal_Id());
                log.info("----状态："+resp.getStat());

                ThreadPoolExecutor cmppDeliverPool = (ThreadPoolExecutor) SpringUtil.getBeanByName("cmppDeliverPool");
                SpringUtil.getBeanByName("cmppDeliverPool");
                cmppDeliverPool.execute(new DeliverRunnable(resp.getMsg_Id_DELIVRD(),resp.getStat()));
            } else {
                //用户回复会打印在这里
                log.info(""+ MsgUtils.bytesToLong(resp.getMsg_Id()));
                log.info(resp.getSrc_terminal_Id());
                log.info(resp.getMsg_Content());
            }
        }
    }

}
