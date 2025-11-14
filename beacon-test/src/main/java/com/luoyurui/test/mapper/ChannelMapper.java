package com.luoyurui.test.mapper;

import com.luoyurui.test.entity.Channel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChannelMapper {

    @Select("select * from channel where is_delete = 0")
    List<Channel> findAll();
}
