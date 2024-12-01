package com.home.ssafyhome.user.mapper;

import com.home.ssafyhome.user.model.UserFavorites;
import java.util.List;

public interface UserFavoritesMapper {
    void insertUserFavorite(UserFavorites userFavorites);
    List<UserFavorites> selectAllUserFavorites();
    List<UserFavorites> selectUserFavoritesByUserId(int userId);
    void updateUserFavorite(UserFavorites userFavorites);
    void deleteUserFavoriteById(int favoriteId);

    void updateUserFavoriteHome(UserFavorites userFavorites);
}
