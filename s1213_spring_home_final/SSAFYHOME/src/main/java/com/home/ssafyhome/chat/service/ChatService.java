package com.home.ssafyhome.chat.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.home.ssafyhome.chat.dto.DistrictCodeDTO;
import com.home.ssafyhome.chat.dto.RequestMessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public interface ChatService {


    void addSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    void sendMessage(WebSocketSession session, RequestMessageDTO requestDto) throws JsonProcessingException;

    void changeSubscription(WebSocketSession session, DistrictCodeDTO codeDTO);
}
