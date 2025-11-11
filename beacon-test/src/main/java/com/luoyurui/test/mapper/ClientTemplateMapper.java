package com.luoyurui.test.mapper;

import com.luoyurui.test.entity.ClientTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClientTemplateMapper {

    @Select("select * from client_template where sign_id = #{signId}")
    List<ClientTemplate> findBySignId(@Param("signId") Long signId);
}
