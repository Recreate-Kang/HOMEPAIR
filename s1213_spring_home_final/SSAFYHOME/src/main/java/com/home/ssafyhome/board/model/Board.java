package com.home.ssafyhome.board.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Board {
    private Integer articleNo;
    private String userId;
    private String subject;
    private String content;
    private Integer hit = 0; // 기본값 0
    private LocalDateTime registerTime = LocalDateTime.now();
}
