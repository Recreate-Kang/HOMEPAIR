package com.home.ssafyhome.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.ssafyhome.chat.dto.DistrictCodeDTO;
import com.home.ssafyhome.chat.dto.RequestMessageDTO;
import com.home.ssafyhome.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connected");
        chatService.addSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        String[] splitMessage = message.getPayload().split("\n\n");

        String[] header = message.getPayload().split("\n");
        String command = header[0];
        System.out.println(command);

        if (command.equals("SEND")) {
            RequestMessageDTO requestDto = objectMapper.readValue(splitMessage[1], RequestMessageDTO.class);
            System.out.println(requestDto+"asdjkgfajklgekluglkfgase");
            chatService.sendMessage(session, requestDto);
        } else if (command.equals("SUBSCRIBE")) {
            DistrictCodeDTO codeDTO = objectMapper.readValue(splitMessage[1], DistrictCodeDTO.class);
            chatService.changeSubscription(session, codeDTO);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatService.removeSession(session);
    }
}
