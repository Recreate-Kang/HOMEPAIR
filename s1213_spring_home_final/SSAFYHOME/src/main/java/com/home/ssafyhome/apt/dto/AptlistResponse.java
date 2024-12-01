package com.home.ssafyhome.apt.dto;


import com.home.ssafyhome.apt.model.AptInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AptlistResponse {
    List<AptInfoDTO> aptlist = new ArrayList<>();

    public static AptlistResponse of(List<AptInfo> aptlist) {
        AptlistResponse rs = new AptlistResponse();
        aptlist.forEach(aptInfo ->
                rs.aptlist.add(AptInfoDTO.of(aptInfo))
        );
        return rs;
    }
}
