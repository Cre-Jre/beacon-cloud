package com.luoyurui.strategy.filter.impl;

import com.luoyurui.common.constant.CacheConstant;
import com.luoyurui.common.model.StandardSubmit;
import com.luoyurui.strategy.client.BeaconCacheClient;
import com.luoyurui.strategy.filter.StrategyFilter;
import com.luoyurui.strategy.util.DFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 敏感词校验
 */
@Slf4j
@Service(value = "dirtyword")
public class DirtyWordStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】   校验ing…………");
        //1.需要获取短信内容'
        String text = submit.getText();
        //2.对短信内容进行分词，并把分词内容存储到集合中

        Set<String> sinterstr = DFAUtil.getDirtyWord(text);

//        Set<String> contents = new HashSet<>();
//        StringReader reader = new StringReader(text);
//            //true为智能分词策略，，，false管理力度更大
//        IKSegmenter ik = new IKSegmenter(reader, true);
//        //lex为null推出
//        Lexeme lex = null;
//        while (true) {
//            try {
//                if ((lex = ik.next()) == null) break;
//            } catch (IOException e) {
//                log.info("【策略模块-敏感词校验】   IK分词器在处理短信内容时，出现异常 e = {}" ,e.getMessage());
//            }
//            contents.add(lex.getLexemeText());
//        }
//        //3.调用Cache缓存模块的交集方法，拿到结果
//        Set<Object> sinterstr = cacheClient.sinterstr(UUID.randomUUID().toString(), CacheConstant.DIRTY_WORD, contents.toArray(new String[]{}));

        //4.根据返回的set集合，判断是否包含敏感词
        if (sinterstr != null && sinterstr.size() > 0) {
            //5、 如果有敏感词，抛出异常 / 其他操作。。
            log.info("【策略模块-敏感词校验】   短信内容包含敏感词信息， dirtyWords = {}",sinterstr);
        }


    }
}
