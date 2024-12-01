package com.home.ssafyhome.apt.service;

import java.util.List;

import com.home.ssafyhome.apt.dto.AptInfoDTO;
import com.home.ssafyhome.apt.dto.AptlistResponse;
import com.home.ssafyhome.apt.model.AptDeal;

public interface AptService {
    AptlistResponse getAptList(String code);

    AptInfoDTO selectApt(String aptSeq);

	List<AptDeal> getDealsByAptSeq(String aptSeq);
}
