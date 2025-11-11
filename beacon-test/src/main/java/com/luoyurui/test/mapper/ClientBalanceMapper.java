package com.luoyurui.test.mapper;

import com.luoyurui.test.entity.ClientBalance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ClientBalanceMapper {

    @Select("select * from client_balance where client_id = #{clientId}")
    ClientBalance findByClientId(@Param("clientId")Long clientId);
}
