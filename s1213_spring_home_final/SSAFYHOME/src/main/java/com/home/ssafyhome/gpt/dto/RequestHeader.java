package com.home.ssafyhome.gpt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestHeader {
    public static final String CONTENT_TYPE = "application/json";
    public static final String YOUR_API_KEY = "";
    public static final String AUTHORIZATION = "Bearer " + YOUR_API_KEY;
}