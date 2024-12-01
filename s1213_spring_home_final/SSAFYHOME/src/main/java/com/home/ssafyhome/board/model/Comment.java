package com.home.ssafyhome.board.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comment {
    private Integer commentId; // 댓글 고유 ID
    private Integer articleNo; // 게시글 ID
    private String userId; // 작성자 ID
    private String content; // 댓글 내용
    private LocalDateTime createdTime ; // 생성 시간
    private Integer parentCommentId; // 부모 댓글 ID (nullable)
}
