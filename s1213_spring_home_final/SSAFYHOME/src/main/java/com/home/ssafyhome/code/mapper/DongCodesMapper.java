package com.home.ssafyhome.code.mapper;

import com.home.ssafyhome.code.model.DongCodes;
import com.home.ssafyhome.code.model.GugunCodes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DongCodesMapper {
    List<DongCodes> findAll();

    List<GugunCodes> findGugunCodes();
}
