package com.home.ssafyhome.apt.dto;

import com.home.ssafyhome.apt.model.AptInfo;
import lombok.Data;

@Data
public class AptInfoDTO {

    String aptSeq;
    String dongCode;
    String aptName;
    String buildYear;
    String latitude;
    String longitude;

    public static AptInfoDTO of(AptInfo aptInfo) {
        AptInfoDTO dto = new AptInfoDTO();
        dto.setAptSeq(aptInfo.getAptSeq());
        dto.setDongCode(aptInfo.getDongCode());
        dto.setAptName(aptInfo.getAptName());
        dto.setBuildYear(aptInfo.getBuildYear());
        dto.setLatitude(aptInfo.getLatitude());
        dto.setLongitude(aptInfo.getLongitude());
        return dto;
    }

}
