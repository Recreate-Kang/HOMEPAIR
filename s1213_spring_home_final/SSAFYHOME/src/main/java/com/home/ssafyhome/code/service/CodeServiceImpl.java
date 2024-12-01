package com.home.ssafyhome.code.service;

import com.home.ssafyhome.code.dto.DongCodeDTO;
import com.home.ssafyhome.code.mapper.DongCodesMapper;
import com.home.ssafyhome.code.model.DongCodes;
import com.home.ssafyhome.code.model.GugunCodes;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CodeServiceImpl implements CodeService {

    private final DongCodesMapper codeMapper;
    private final DongCodesMapper dongCodesMapper;

    CodeServiceImpl(DongCodesMapper codeMapper, DongCodesMapper dongCodesMapper) {
        this.codeMapper = codeMapper;
        this.dongCodesMapper = dongCodesMapper;
    }

    public List<DongCodeDTO> getDistrictCodesAsTree() {
        List<DongCodes> allDongCodes = codeMapper.findAll();

        Map<String, List<DongCodes>> sidoMap = allDongCodes.stream()
                .filter(dongCode -> dongCode.getSidoName() != null)
                .collect(Collectors.groupingBy(dongCode -> dongCode.getSidoName() != null ? dongCode.getSidoName() : "Unknown"));

        List<DongCodeDTO> tree = new ArrayList<>();
        for (String sido : sidoMap.keySet()) {
            String sidoCode = sidoMap.get(sido).get(0).getDongCode().substring(0, 2);
            DongCodeDTO sidoDto = new DongCodeDTO(sido, sidoCode);

            Map<String, List<DongCodes>> gugunMap = sidoMap.get(sido).stream()
                    .filter(dongCode -> dongCode.getGugunName() != null)
                    .collect(Collectors.groupingBy(dongCode -> dongCode.getGugunName() != null ? dongCode.getGugunName() : "Unknown"));

            List<DongCodeDTO> gugunList = new ArrayList<>();
            for (String gugun : gugunMap.keySet()) {
                String gugunCode = gugunMap.get(gugun).get(0).getDongCode().substring(0, 5);
                DongCodeDTO gugunDto = new DongCodeDTO(gugun, gugunCode);

                List<DongCodeDTO> dongList = gugunMap.get(gugun).stream()
                        .filter(dongCode -> dongCode.getDongName() != null)
                        .map(dongCode -> new DongCodeDTO(dongCode.getDongName(), dongCode.getDongCode()))
                        .collect(Collectors.toList());

                gugunDto.setChildren(dongList);
                gugunList.add(gugunDto);
            }

            sidoDto.setChildren(gugunList);
            tree.add(sidoDto);
        }
        return tree;
    }

    @Override
    public List<GugunCodes> getGugunCodes() {
        return dongCodesMapper.findGugunCodes();
    }
}
