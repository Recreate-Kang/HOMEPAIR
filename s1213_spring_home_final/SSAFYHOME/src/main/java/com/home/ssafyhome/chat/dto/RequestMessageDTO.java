package com.home.ssafyhome.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class RequestMessageDTO {
    private String jwtToken;
    private String chatName;
    @CreatedDate
    private String chatTime;
    private String district;
    private String chatMessage;

    // 추가: 클라이언트에서 보낸 sender 필드
    private String sender;
}
