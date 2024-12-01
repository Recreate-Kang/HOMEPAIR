package com.home.ssafyhome.apt.model;

import lombok.Data;

@Data
public class AptInfo {
    private String aptSeq;  // varchar(20) -> String
    private String dongCode;
    private String sggCode;  // varchar(5) -> String
    private String umdCode;  // varchar(5) -> String
    private String umdName;  // varchar(20) -> String
    private String jibun;  // varchar(10) -> String
    private String roadNameSggCd;  // added missing field for road_nm_sgg_cd (varchar(5))
    private String roadName;  // varchar(20) -> String
    private String roadNameBonbun;  // varchar(10) -> String
    private String roadNameBubun;  // varchar(10) -> String
    private String aptName;  // varchar(40) -> String
    private String buildYear;  // varchar -> String (기존 int -> 수정)
    private String latitude;  // varchar(45) -> String
    private String longitude;  // varchar(45) -> String
}