package com.home.ssafyhome.apt.model;

import lombok.Data;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class AptDeal {
    private int no; // 거래번호
    private String aptSeq; // 아파트 ID
    private String aptDong; // 동 정보 (nullable)
    private int floor; // 층
    private int dealYear; // 거래 연도
    private int dealMonth; // 거래 월
    private int dealDay; // 거래 일
    private double excluUseAr; // 전용 면적
    private double dealAmount; // 거래 금액
    private double pricePerArea; // 거래 금액 / 전용 면적
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dealDate; // 거래 날짜 (연-월-일 결합)
}