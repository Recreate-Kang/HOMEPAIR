package com.home.ssafyhome.code.controller;

import com.home.ssafyhome.code.dto.DongCodeDTO;
import com.home.ssafyhome.code.service.CodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("code")
public class CodeController {
    CodeService codeService;

    CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("")
    ResponseEntity<List<DongCodeDTO>> getDongCode() {
        return ResponseEntity.ok().body(codeService.getDistrictCodesAsTree());
    }
}
