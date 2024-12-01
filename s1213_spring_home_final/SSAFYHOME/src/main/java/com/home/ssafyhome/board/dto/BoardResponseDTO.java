package com.home.ssafyhome.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponseDTO {
    private Integer articleNo;       // 게시글 번호
    private String userId;           // 작성자 ID
    private String subject;          // 제목
    private String content;          // 내용
    private Integer hit;             // 조회수
    private LocalDateTime registerTime; // 등록 시간
}