package com.home.ssafyhome.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserFavoriteListDTO {
    private List<UserFavoritesDTO> userFavorites;

    public static UserFavoriteListDTO of(List<UserFavoritesDTO> list) {
        UserFavoriteListDTO dto = new UserFavoriteListDTO();
        dto.setUserFavorites(list);
        return dto;
    }
}
