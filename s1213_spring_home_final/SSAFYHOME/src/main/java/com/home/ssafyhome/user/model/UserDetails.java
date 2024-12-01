package com.home.ssafyhome.user.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class UserDetails {
    private int userId; // Primary Key
    private Integer age; // 나이 (NULL 가능)
    private String gender; // ENUM 값: 'male', 'female', 'other'
    private String occupation; // 직업
    private String hobbies; // 취미
    private String maritalStatus; // ENUM 값: 'single', 'married', 'divorced', 'widowed'
    private Float currentArea; // 평수 (NULL 가능)
    private String hasPets; // 반려동물 유무
    private String bio; // 자기소개
    private Timestamp updatedAt; // 최종 수정 시간
}
