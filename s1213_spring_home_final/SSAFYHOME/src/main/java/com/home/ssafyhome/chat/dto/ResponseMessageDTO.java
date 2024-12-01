package com.home.ssafyhome.chat.dto;

import lombok.Data;

@Data
public class ResponseMessageDTO {
    String chatName;
    String chatMessage;

    public static ResponseMessageDTO of(RequestMessageDTO request) {
        ResponseMessageDTO response = new ResponseMessageDTO();
        response.chatName = request.getChatName();
        response.chatMessage = request.getChatMessage();
        return response;
    }
}
