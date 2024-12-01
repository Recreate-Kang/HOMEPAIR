package com.home.ssafyhome.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyDTO {
    private String model = "gpt-4o-2024-08-06"; // 모델 이름
    private List<Message> messages; // Message 리스트
    private ResponseFormat response_format = new ResponseFormat();

    // Message 클래스
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {
        private String role;    // system 또는 user
        private String content; // JSON으로 변환될 데이터
        private String name;    // user ID (Optional)
    }

    // ResponseFormat 클래스
    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseFormat {
        private String type = "json_schema";

        @JsonProperty("json_schema")
        private Map<String, Object> json_schema;

        public ResponseFormat() {
            this.json_schema = Map.of(
                    "name", "apartment_evaluation",
                    "schema", Map.of(
                            "type", "object",
                            "properties", Map.of(
                                    "총평", Map.of(
                                            "type", "string",
                                            "enum", List.of("매우추천", "추천", "평범", "조금아쉬움", "다른곳을찾아봐요")
                                    ),
                                    "고려한 요소", Map.of(
                                            "type", "array",
                                            "items", Map.of("type", "string")
                                    ),
                                    "긍정적 요소", Map.of(
                                            "type", "array",
                                            "items", Map.of(
                                                    "type", "object",
                                                    "properties", Map.of(
                                                            "항목", Map.of("type", "string"),
                                                            "설명", Map.of("type", "string")
                                                    ),
                                                    "required", List.of("항목", "설명"),
                                                    "additionalProperties", false
                                            )
                                    ),
                                    "부정적 요소", Map.of(
                                            "type", "array",
                                            "items", Map.of(
                                                    "type", "object",
                                                    "properties", Map.of(
                                                            "항목", Map.of("type", "string"),
                                                            "설명", Map.of("type", "string")
                                                    ),
                                                    "required", List.of("항목", "설명"),
                                                    "additionalProperties", false
                                            )
                                    ),
                                    "기타 요소", Map.of(
                                            "type", "array",
                                            "items", Map.of(
                                                    "type", "object",
                                                    "properties", Map.of(
                                                            "항목", Map.of("type", "string"),
                                                            "설명", Map.of("type", "string")
                                                    ),
                                                    "required", List.of("항목", "설명"),
                                                    "additionalProperties", false
                                            )
                                    ),
                                    "대체 추천 매물", Map.of(
                                            "type", "array",
                                            "items", Map.of(
                                                    "type", "object",
                                                    "properties", Map.of(
                                                            "지역", Map.of("type", "string"),
                                                            "매물명", Map.of("type", "string"),
                                                            "설명", Map.of("type", "string")
                                                    ),
                                                    "required", List.of("지역", "매물명", "설명"),
                                                    "additionalProperties", false
                                            )
                                    )
                            ),
                            "required", List.of("총평", "고려한 요소", "긍정적 요소", "부정적 요소", "기타 요소", "대체 추천 매물"),
                            "additionalProperties", false
                    ),
                    "strict", true
            );
        }
    }
}