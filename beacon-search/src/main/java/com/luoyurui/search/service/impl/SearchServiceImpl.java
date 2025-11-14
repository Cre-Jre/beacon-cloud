package com.luoyurui.search.service.impl;

import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.exception.ElasticSearchException;
import com.luoyurui.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * 添加成功的result
     */
    private final String CREATED = "created";

    @Autowired
    private RestHighLevelClient restHighLevelClient;


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
}
