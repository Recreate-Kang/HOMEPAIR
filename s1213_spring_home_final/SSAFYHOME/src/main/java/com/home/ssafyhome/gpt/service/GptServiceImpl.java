package com.home.ssafyhome.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.ssafyhome.gpt.dto.RequestBodyDTO;
import com.home.ssafyhome.gpt.dto.RequestHeader;
import com.home.ssafyhome.gpt.dto.TotalEvaluationRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class GptServiceImpl implements GptService {

    @Override
    public String processRequest(TotalEvaluationRequest requestDto) throws JsonProcessingException {
        RequestBodyDTO.Message message = buildMessage(requestDto);
        RequestBodyDTO requestBodyDTO = new RequestBodyDTO();
        requestBodyDTO.setModel("gpt-4o-2024-08-06");
        requestBodyDTO.setMessages(new ArrayList<>());
        requestBodyDTO.getMessages().add(message);
        requestBodyDTO.getMessages().add(buildSystemMessage(requestDto));
        return sendRequest(requestBodyDTO);
    }

    private RequestBodyDTO.Message buildMessage(TotalEvaluationRequest requestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBodyDTO.Message message = new RequestBodyDTO.Message();
        message.setRole("user");
        message.setName(String.valueOf(requestDto.getUserDetailsDTOList().get(0).getUserId()));
        HashMap<String, Object> contentMap = new HashMap<>();
        contentMap.put("유저정보", requestDto.getUserDetailsDTOList());
        contentMap.put("유저선호부동산", requestDto.getUserFavoritesDTOList());
        contentMap.put("대상부동산 주변정보", requestDto.getInfraListResponse());
        contentMap.put("대상부동산", requestDto.getAptInfoDTO());
        contentMap.put("대상부동산 거래내역", requestDto.getAptDeal());
        String contentJson = objectMapper.writeValueAsString(contentMap);
        message.setContent(contentJson);
        return message;
    }

    private RequestBodyDTO.Message buildSystemMessage(TotalEvaluationRequest requestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBodyDTO.Message message = new RequestBodyDTO.Message();
        message.setRole("system");
        HashMap<String, Object> contentMap = new HashMap<>();
        contentMap.put("instruction", "요구사항, 엄격한 부동산 투자자의 입장이 되어 내가 유저의 입장에서(개인 신상을 상세기 고려) 이 곳을 고려한다면 이라는 가정하에 비교 수치화된 평가해줘 \"대상부동산\"을");
        String contentJson = objectMapper.writeValueAsString(contentMap);
        message.setContent(contentJson);
        return message;
    }

    private String sendRequest(RequestBodyDTO requestBodyDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Serialize the request body to JSON
        String requestJson = objectMapper.writeValueAsString(requestBodyDTO);

        // Send request to GPT
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", RequestHeader.AUTHORIZATION);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        // Send POST request and get the response
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Parse GPT response content
        String parsedContent = parseGptResponseContent(response.getBody());
        System.out.println("Parsed Content: " + parsedContent);

        // Return the parsed content
        return parsedContent;
    }

    private String parseGptResponseContent(String gptResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the entire response JSON
        JsonNode responseJson = objectMapper.readTree(gptResponse);

        // Navigate to the "content" field in the response JSON
        JsonNode contentNode = responseJson
                .path("choices")
                .get(0)
                .path("message")
                .path("content");

        // Ensure "content" field exists and return its value as a formatted JSON string
        if (!contentNode.isMissingNode()) {
            // Parse the content JSON string into a formatted JSON string
            return objectMapper.readTree(contentNode.asText()).toPrettyString();
        } else {
            throw new JsonProcessingException("Content field not found in GPT response") {
            };
        }
    }
}
