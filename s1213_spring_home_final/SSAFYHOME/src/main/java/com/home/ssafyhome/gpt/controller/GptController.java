package com.home.ssafyhome.gpt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.home.ssafyhome.gpt.dto.TotalEvaluationRequest;
import com.home.ssafyhome.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("GPT")
public class GptController {
    private final GptService gptService;

    // GptPlaceAnalysisResponse askGPTPlaceAnalysis()
    @PostMapping("evaluation")
    public ResponseEntity<String> requestAPTevaluation(@RequestBody TotalEvaluationRequest requestDto) throws JsonProcessingException {
        String jsonResponse = gptService.processRequest(requestDto);

        // JSON 출력
        //System.out.println(jsonResponse);
        return ResponseEntity.ok().body(jsonResponse);
    }
}
