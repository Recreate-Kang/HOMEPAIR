package com.home.ssafyhome.user.mapper;

import com.home.ssafyhome.user.model.UserDetails;
import java.util.List;

public interface UserDetailsMapper {
    void insertUserDetails(UserDetails userDetails);
    List<UserDetails> selectAllUserDetails();
    UserDetails selectUserDetailsByUserId(int userId);
    void updateUserDetails(UserDetails userDetails);
    void deleteUserDetailsById(int detailId);
}