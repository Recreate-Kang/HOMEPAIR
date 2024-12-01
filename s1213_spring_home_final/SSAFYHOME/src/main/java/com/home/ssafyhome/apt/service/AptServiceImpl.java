package com.home.ssafyhome.apt.service;

import com.home.ssafyhome.apt.dto.AptInfoDTO;
import com.home.ssafyhome.apt.dto.AptlistResponse;
import com.home.ssafyhome.apt.mapper.AptMapper;
import com.home.ssafyhome.apt.model.AptDeal;
import com.home.ssafyhome.apt.model.AptInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AptServiceImpl implements AptService {

    AptMapper aptMapper;

    AptServiceImpl(AptMapper aptMapper) {
        this.aptMapper = aptMapper;
    }

    @Override
    public AptlistResponse getAptList(String code) {
        List<AptInfo> aptlist = aptMapper.findAllByDongCodeStartingWith(code.trim());
        return AptlistResponse.of(aptlist);
    }

    @Override
    public AptInfoDTO selectApt(String aptSeq) {
        AptInfo aptInfo = aptMapper.findOneByAptSeq(aptSeq);
        return AptInfoDTO.of(aptInfo);
    }

	@Override
	public List<AptDeal> getDealsByAptSeq(String aptSeq) {
        return aptMapper.selectDealsByAptSeq(aptSeq);
	}
}
