package com.home.ssafyhome.user.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserFavorites {
    private int favoriteId;
    private int userId;
    private String aptSeq;
    private String myPlaceName;
    private LocalDateTime createdAt; // TIMESTAMP 필드

    // Lombok의 @Data가 Getters, Setters, ToString, EqualsAndHashCode를 자동 생성
}