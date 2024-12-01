package com.home.ssafyhome.apt.mapper;


import com.home.ssafyhome.apt.model.AptDeal;
import com.home.ssafyhome.apt.model.AptInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AptMapper {
    List<AptInfo> findAllByDongCodeStartingWith(@Param("code") String code);

    AptInfo findOneByAptSeq(@Param("seqCode") String seqCode);
    List<AptDeal> selectDealsByAptSeq(@Param("aptSeq") String aptSeq);
}
