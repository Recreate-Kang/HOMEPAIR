package com.home.ssafyhome.board.dto;

import lombok.Data;

@Data
public class BoardRequestDTO {
    private Integer articleNo;
    private String userId;   // 작성자 ID
    private String subject;  // 제목
    private String content;  // 내용
}
