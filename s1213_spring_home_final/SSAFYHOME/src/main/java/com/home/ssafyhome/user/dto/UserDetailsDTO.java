package com.home.ssafyhome.user.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDetailsDTO {
    private int userId; // PK
    private Integer age; // Null 허용
    private String gender; // ENUM: 'male', 'female', 'other'
    private String occupation; // 직업
    private String hobbies; // 취미
    private String maritalStatus; // ENUM: 'single', 'married', 'divorced', 'widowed'
    private Float currentArea; // Null 허용 (거주 평수)
    private String hasPets; // TINYINT(1) -> Boolean으로 매핑
    private String bio; // 간략한 자기소개
    private Timestamp updatedAt; // 데이터 최종 수정 시간 (TIMESTAMP)
}