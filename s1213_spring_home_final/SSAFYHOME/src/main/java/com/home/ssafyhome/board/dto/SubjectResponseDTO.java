package com.home.ssafyhome.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubjectResponseDTO {
    private Integer articleNo;       // 게시글 번호
    private String subject;          // 제목
    private String userId;           // 작성자 ID
    private Integer hit;
    private LocalDateTime registerTime; // 작성 시간
}
