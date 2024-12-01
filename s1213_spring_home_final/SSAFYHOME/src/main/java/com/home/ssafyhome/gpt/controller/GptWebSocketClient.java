package com.home.ssafyhome.gpt.controller;

import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GptWebSocketClient extends Endpoint {

    private static final String GPTURL = "wss://api.openai.com/v1/realtime";
    private static final String GPTQUERY = "?model=gpt-4o-realtime-preview-2024-10-01";
    private static final String API_KEY = "your_actual_api_key"; // API 키 입력
    private Session session;

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message received: " + message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed. Reason: " + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error occurred: " + throwable.getMessage());
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void connectToGPTServer() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                    .configurator(new ClientEndpointConfig.Configurator() {
                        @Override
                        public void beforeRequest(java.util.Map<String, List<String>> headers) {
                            headers.put("Authorization", Collections.singletonList("Bearer " + API_KEY));
                            headers.put("OpenAI-Beta", Collections.singletonList("realtime=v1"));
                        }
                    })
                    .build();
            // `GptWebSocketClient.class` 전달
            container.connectToServer(GptWebSocketClient.class, config, URI.create(GPTURL + GPTQUERY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    void init() {
        System.out.println("GPT WebSocket Bean created.");
        try {
            connectToGPTServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
