package com.home.ssafyhome.user.dto;

import lombok.Data;

@Data
public class UserFavoritesDTO {
    private int favoriteId;
    private int userId;
    private String aptSeq;
    private String myPlaceName;
}