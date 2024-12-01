package com.home.ssafyhome.user.service;

import com.home.ssafyhome.user.dto.UserDetailsDTO;
import com.home.ssafyhome.user.dto.UserFavoriteListDTO;
import com.home.ssafyhome.user.dto.UserFavoritesDTO;
import com.home.ssafyhome.user.mapper.UserDetailsMapper;
import com.home.ssafyhome.user.mapper.UserFavoritesMapper;
import com.home.ssafyhome.user.model.UserDetails;
import com.home.ssafyhome.user.model.UserFavorites;
import com.home.ssafyhome.user.util.DtoEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserFavoritesMapper userFavoritesMapper;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserFavoriteListDTO getUserFavorite(Integer userId) {
        List<UserFavoritesDTO> favoritesDTOList = new java.util.ArrayList<>();
        for(UserFavorites favorites: userFavoritesMapper.selectUserFavoritesByUserId(userId)) {
            favoritesDTOList.add(DtoEntityConverter.convertToDto(favorites, UserFavoritesDTO.class));
        }
        return UserFavoriteListDTO.of(favoritesDTOList);
    }

    @Override
    public void updateUserFavorite(UserFavoritesDTO userFavoritesDTO) {
        userFavoritesMapper.updateUserFavorite(DtoEntityConverter.convertToEntity(userFavoritesDTO,UserFavorites.class));
    }

    @Override
    public UserDetailsDTO getUserDetail(Integer userId) {
        return com.home.ssafyhome.user.util.DtoEntityConverter.convertToDto(userDetailsMapper.selectUserDetailsByUserId(userId), com.home.ssafyhome.user.dto.UserDetailsDTO.class);
    }

    @Override
    public void updateUserDetail(UserDetailsDTO userDetailsDTO) {
        userDetailsMapper.updateUserDetails(DtoEntityConverter.convertToEntity(userDetailsDTO, UserDetails.class));
    }

    @Override
    public void putUserFavorite(UserFavoritesDTO userFavoritesDTO) {
        if(userFavoritesDTO.getMyPlaceName().equals("HOME")){
            userFavoritesMapper.updateUserFavoriteHome(DtoEntityConverter.convertToEntity(userFavoritesDTO,UserFavorites.class));
            return;
        }
        userFavoritesMapper.insertUserFavorite(DtoEntityConverter.convertToEntity(userFavoritesDTO,UserFavorites.class));
    }

    @Override
    public void deleteUserFavorite(UserFavoritesDTO userFavoritesDTO) {
        if(userFavoritesDTO.getMyPlaceName().equals("HOME")){
            userFavoritesDTO.setAptSeq(null);
            userFavoritesMapper.updateUserFavoriteHome(DtoEntityConverter.convertToEntity(userFavoritesDTO,UserFavorites.class));
            return;
        }
        userFavoritesMapper.deleteUserFavoriteById(userFavoritesDTO.getFavoriteId());
    }
}
