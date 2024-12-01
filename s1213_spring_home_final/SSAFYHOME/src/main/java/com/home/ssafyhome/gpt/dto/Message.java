package com.home.ssafyhome.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String role;    // system 또는 user
    private List<?> content; // JSON으로 변환될 데이터
    private String name;    // user ID (Optional)
}