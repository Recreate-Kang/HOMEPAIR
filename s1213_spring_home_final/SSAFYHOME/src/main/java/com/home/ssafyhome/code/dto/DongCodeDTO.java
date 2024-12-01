package com.home.ssafyhome.code.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DongCodeDTO {
    private String name;
    private String code;
    private List<DongCodeDTO> children;

    // 시도와 구군용 생성자 (children 포함)
    public DongCodeDTO(String name) {
        this.name = name;
        this.children = new ArrayList<>(); // 시도와 구군만 children 생성
    }

    // 동 이름과 동 코드를 위한 생성자 (children 미포함)
    public DongCodeDTO(String name, String code) {
        this.name = name;
        this.code = code;
        this.children = null; // 동은 children이 없도록 설정
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<DongCodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DongCodeDTO> children) {
        this.children = children;
    }
}