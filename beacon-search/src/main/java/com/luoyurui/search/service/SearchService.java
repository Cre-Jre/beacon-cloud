package com.luoyurui.search.service;

import java.io.IOException;
import java.util.Map;

public interface SearchService {

    /**
     * 向es中添加一行文档
     * @param index   索引信息
     * @param id   文档id
     * @param json  文档内容
     */
    void index(String index, String id, String json) throws IOException;

    /**
     * 查看指定索引中的文档是否存在
     * @param index
     * @param id
     * @return
     * @throws IOException
     */
    boolean exists(String index, String id) throws IOException;

    /**
     * 修改文档信息
     * @param index
     * @param id
     * @param doc  要修改的key,value集合
     */
    void update(String index, String id, Map<String, Object> doc) throws IOException;
}
