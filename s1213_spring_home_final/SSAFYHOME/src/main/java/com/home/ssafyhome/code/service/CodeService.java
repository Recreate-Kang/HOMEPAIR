package com.home.ssafyhome.code.service;

import com.home.ssafyhome.code.dto.DongCodeDTO;
import com.home.ssafyhome.code.model.GugunCodes;

import java.util.List;

public interface CodeService {
    public List<DongCodeDTO> getDistrictCodesAsTree();

    public List<GugunCodes> getGugunCodes();
}
