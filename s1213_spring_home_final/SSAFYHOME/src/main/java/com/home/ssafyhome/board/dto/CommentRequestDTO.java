package com.home.ssafyhome.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequestDTO {
    // Getters and Setters
    private Integer commentId; // 댓글 고유 ID
    private String userId; // 작성자 ID
    private String content; // 댓글 내용
    private LocalDateTime createdTime = LocalDateTime.now(); // 생성 시간
    private Integer parentCommentId; // 부모 댓글 ID (nullable)

}
