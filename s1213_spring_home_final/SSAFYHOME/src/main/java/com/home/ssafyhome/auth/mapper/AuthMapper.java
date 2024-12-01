package com.home.ssafyhome.auth.mapper;

import com.home.ssafyhome.auth.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {

    User findByUserIdValueAndUserpw(@Param("userId") String userId, @Param("password") String password);

    void insertUser(User user); // 회원가입에 필요한 메서드 추가
}
