package com.home.ssafyhome.user.controller;

import com.home.ssafyhome.user.dto.UserDetailsDTO;
import com.home.ssafyhome.user.dto.UserFavoriteListDTO;
import com.home.ssafyhome.user.dto.UserFavoritesDTO;
import com.home.ssafyhome.user.dto.UserIdRequest;
import com.home.ssafyhome.user.model.UserFavorites;
import com.home.ssafyhome.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/favorite")
    public ResponseEntity<UserFavoriteListDTO> getUserFavorite(@RequestBody UserIdRequest userId) {
        return ResponseEntity.ok().body(userService.getUserFavorite(userId.getUserId()));
    }
    @PatchMapping("favorite")
    public ResponseEntity<Void> updateUserFavorite(@RequestBody UserFavoritesDTO userFavoritesDTO) {
        userService.updateUserFavorite(userFavoritesDTO);
        return ResponseEntity.ok().build();
    }
    @PutMapping("favorite")
    public ResponseEntity<UserFavorites> addUserFavorite(@RequestBody UserFavoritesDTO userFavoritesDTO) {
        userService.putUserFavorite(userFavoritesDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("favorite")
    public ResponseEntity<Void> deleteUserFavorite(@RequestBody UserFavoritesDTO userFavoritesDTO) {
        userService.deleteUserFavorite(userFavoritesDTO);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/detail")
    public ResponseEntity<UserDetailsDTO> getUserDetail(@RequestBody UserIdRequest userId) {
        return ResponseEntity.ok().body(userService.getUserDetail(userId.getUserId()));
    }
    @PatchMapping("/detail")
    ResponseEntity<Void> updateUserDetail(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.updateUserDetail(userDetailsDTO);
        return ResponseEntity.ok().build();
    }
}
