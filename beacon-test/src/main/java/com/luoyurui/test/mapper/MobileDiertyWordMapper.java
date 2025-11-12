package com.luoyurui.test.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MobileDiertyWordMapper {

    @Select("select dirtyword from mobile_dirtyword")
    List<String> findDirtyWord();
}
