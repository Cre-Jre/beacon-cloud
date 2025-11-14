package com.luoyurui.test.mapper;

import com.luoyurui.test.entity.Channel;
import com.luoyurui.test.entity.ClientChannel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClientChannelMapper {

    @Select("select * from client_channel where is_delete = 0")
    List<ClientChannel> findAll();
}
