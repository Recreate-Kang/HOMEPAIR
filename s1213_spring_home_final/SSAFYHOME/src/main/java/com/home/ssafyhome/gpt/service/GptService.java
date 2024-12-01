package com.home.ssafyhome.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.home.ssafyhome.gpt.dto.TotalEvaluationRequest;

public interface GptService {
    String processRequest(TotalEvaluationRequest requestDto) throws JsonProcessingException;
}
