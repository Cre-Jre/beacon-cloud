package com.luoyurui.search.service.impl;

import com.luoyurui.common.constant.RabbitMQConstants;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.ElasticSearchException;
import com.luoyurui.common.model.StandardReport;
import com.luoyurui.search.service.SearchService;
import com.luoyurui.search.utils.SearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * 添加成功的result
     */
    private final String CREATED = "created";

    private final String UPDATED = "updated";

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void index(String index, String id, String json) throws IOException {
        //1.构建插入数据的request
        IndexRequest indexRequest = new IndexRequest();

        //2.给request对象封装对应的索引信息，文档id，和文档内容
        indexRequest.index(index);
        indexRequest.id(id);
        indexRequest.source(json, XContentType.JSON);
        //3.将request发送到es
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        //4.校验是否添加成功
        String lowercase = response.getResult().getLowercase();
        if(!CREATED.equals(lowercase)){
            // 添加失败！！
            log.error("【搜索模块-写入数据失败】 index = {},id = {},json = {},result = {}",index,id,json,lowercase);
            throw new ElasticSearchException(ExceptionEnums.SEARCH_INDEX_ERROR);
        }
        log.info("【搜索模块-写入数据成功】 索引添加成功index = {},id = {},json = {},result = {}",index,id,json,lowercase);
    }

    @Override
    public boolean exists(String index, String id) throws IOException {
        //构建GetRequest，看索引是否存在
        GetRequest getRequest = new GetRequest();
        //指定索引信息和文档id
        getRequest.index(index);
        getRequest.id(id);
        //基于restHighLevelClient将查询指定的id文档是否存在请求投递出
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);

        return exists;
    }

    @Override
    public void update(String index, String id, Map<String, Object> doc) throws IOException {
        //1.基于exists方法，查询当前文档是否存在
        boolean exists = exists(index, id);
        if (!exists) {
            //当前文档不存在
            StandardReport standardReport = SearchUtils.get();
            if (standardReport.getReUpdate()) {
                //第二次获取投递消息
                log.error("【搜索模块-修改日志】 修改日志失败，standardReport = {}",standardReport);
            } else {
                //第一次投递，可以再次把消息丢到mq中
                //设置为true开始第二次消息投递
                standardReport.setReUpdate(true);
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_GATEWAY_NORMAL_QUEUE,standardReport);
            }
            SearchUtils.remove();
            return;
        }
        //2. 文档是存在的，直接做修改操作
        UpdateRequest request = new UpdateRequest();

        request.index(index);
        request.id(id);
        request.doc(doc);

        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        String result = update.getResult().getLowercase();
        if(!UPDATED.equals(result)){
            // 添加失败！！
            log.error("【搜索模块-修改日志失败】 index = {},id = {},doc = {}",index,id,doc);
            throw new ElasticSearchException(ExceptionEnums.SEARCH_UPDATE_ERROR);
        }
        log.info("【搜索模块-修改日志成功】 文档修改成功index = {},id = {},doc = {}",index,id,doc);
    }
}
