package com.home.ssafyhome.gpt.dto;

import com.home.ssafyhome.apt.dto.AptInfoDTO;
import com.home.ssafyhome.apt.model.AptDeal;
import com.home.ssafyhome.infra.dto.InfraListResponse;
import com.home.ssafyhome.user.dto.UserDetailsDTO;
import com.home.ssafyhome.user.dto.UserFavoritesDTO;
import lombok.Data;

import java.util.List;

@Data
public class TotalEvaluationRequest {
    List<UserDetailsDTO> userDetailsDTOList;
    List<UserFavoritesDTO> userFavoritesDTOList;
    InfraListResponse infraListResponse;
    AptInfoDTO aptInfoDTO;
    AptDeal aptDeal;

}
