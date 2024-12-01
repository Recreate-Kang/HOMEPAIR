package com.home.ssafyhome.user.service;

import com.home.ssafyhome.user.dto.UserDetailsDTO;
import com.home.ssafyhome.user.dto.UserFavoriteListDTO;
import com.home.ssafyhome.user.dto.UserFavoritesDTO;

public interface UserService {

    UserFavoriteListDTO getUserFavorite(Integer username);

    void updateUserFavorite(UserFavoritesDTO userFavoritesDTO);

    UserDetailsDTO getUserDetail(Integer userName);

    void updateUserDetail(UserDetailsDTO userDetailsDTO);

    void putUserFavorite(UserFavoritesDTO userFavoritesDTO);

    void deleteUserFavorite(UserFavoritesDTO userFavoritesDTO);
}
