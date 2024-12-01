package com.home.ssafyhome.code.model;

import lombok.Data;

@Data
public class GugunCodes {
    private String code;       // left(dong_code, 5) 값
    private String sidoName;   // sido_name 컬럼
    private String gugunName;  // gugun_name 컬럼

}
