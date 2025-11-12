package com.luoyurui.test.mapper;

import com.luoyurui.test.entity.MobileTransfer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MobileTransferMapper {

    @Select("select transfer_number,now_isp from mobile_transfer where is_transfer = 1 and is_delete = 0")
    List<MobileTransfer> findAll();
}
