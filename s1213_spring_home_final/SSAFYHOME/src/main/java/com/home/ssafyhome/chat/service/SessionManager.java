package com.home.ssafyhome.chat.service;

import com.home.ssafyhome.chat.dto.DistrictCodeDTO;
import com.home.ssafyhome.code.model.GugunCodes;
import com.home.ssafyhome.code.service.CodeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionManager {

    private final CodeService codeService;
    private HashMap<String, HashMap<String, WebSocketSession>> districtSession;
    private HashMap<String, String> sessionSubscription;

    @PostConstruct
    public void init() {
        this.districtSession = new HashMap<>();
        this.sessionSubscription = new HashMap<>();
        List<GugunCodes> codes = codeService.getGugunCodes();
        for (GugunCodes code : codes) {
            districtSession.put(code.getCode(), new HashMap<>());
        }
    }

    public void changeSubstription(WebSocketSession session, DistrictCodeDTO newCode) {
        String oldCode = sessionSubscription.get(session.getId());
        if (oldCode != null) {
            districtSession.get(oldCode).remove(session.getId());
        }
        sessionSubscription.put(session.getId(), newCode.getDistrictCode());
        districtSession.get(newCode.getDistrictCode()).put(session.getId(), session);
    }

    public void createConnection(WebSocketSession session) {
        sessionSubscription.put(session.getId(), null);
        System.out.println("sessisisn연결하기------------" + session);
    }

    public void disconnectConnection(WebSocketSession session) {
        session.getId();
        System.out.println();
        System.out.println("Sesison연결끊기----------" + session);
        String connectedCode = sessionSubscription.get(session.getId());
        if (connectedCode != null) districtSession.get(connectedCode).remove(session.getId());
        sessionSubscription.remove(session.getId());
    }

    List<WebSocketSession> getDistrictSessions(String code) {
        System.out.println(code);
        return districtSession.get(code).values().stream().toList();
    }
}
