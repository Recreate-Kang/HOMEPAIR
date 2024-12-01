package com.home.ssafyhome.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.ssafyhome.chat.dto.DistrictCodeDTO;
import com.home.ssafyhome.chat.dto.RequestMessageDTO;
import com.home.ssafyhome.chat.dto.ResponseMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private static ObjectMapper objMapper = new ObjectMapper();
    private final SessionManager sessionManager;

    @Override
    public void addSession(WebSocketSession session) {
        sessionManager.createConnection(session);
    }

    @Override
    public void removeSession(WebSocketSession session) {
        sessionManager.disconnectConnection(session);
    }

    @Override
    public void sendMessage(WebSocketSession session, RequestMessageDTO requestDto) throws JsonProcessingException {
        List<WebSocketSession> sessionList = sessionManager.getDistrictSessions(requestDto.getDistrict());
        System.out.println(requestDto);
        ResponseMessageDTO resMessage = ResponseMessageDTO.of(requestDto);
        String jsonMessage = objMapper.writeValueAsString(resMessage);
        TextMessage message = new TextMessage(jsonMessage);
        sessionList.forEach(sess -> {
            try {
                System.out.println("StringMEssage" + message);
                sess.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void changeSubscription(WebSocketSession session, DistrictCodeDTO codeDTO) {
        sessionManager.changeSubstription(session, codeDTO);
    }
}
