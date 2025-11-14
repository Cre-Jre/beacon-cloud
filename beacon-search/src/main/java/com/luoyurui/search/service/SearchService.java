package com.luoyurui.search.service;

import java.io.IOException;

public interface SearchService {

    /**
     * 向es中添加一行文档
     * @param index   索引信息
     * @param id   文档id
     * @param json  文档内容
     */
    void index(String index, String id, String json) throws IOException;
}
