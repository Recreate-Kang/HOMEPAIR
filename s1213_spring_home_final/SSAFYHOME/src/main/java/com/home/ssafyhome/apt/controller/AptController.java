package com.home.ssafyhome.apt.controller;

import com.home.ssafyhome.apt.dto.AptInfoDTO;
import com.home.ssafyhome.apt.dto.AptlistResponse;
import com.home.ssafyhome.apt.dto.RequestCode;
import com.home.ssafyhome.apt.dto.RequestSelectApt;
import com.home.ssafyhome.apt.model.AptDeal;
import com.home.ssafyhome.apt.service.AptService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apt")
public class AptController {

    AptService aptService;

    AptController(AptService aptService) {
        this.aptService = aptService;
    }

    @PostMapping("aptlist")
    ResponseEntity<AptlistResponse> getAptList(@RequestBody RequestCode code) {
        return ResponseEntity.ok().body(aptService.getAptList(code.getCode()));
    }

    @PostMapping("select")
    ResponseEntity<AptInfoDTO> selectApt(@RequestBody RequestSelectApt aptSeq) {
        return ResponseEntity.ok().body(aptService.selectApt(aptSeq.getAptSeq()));
    }
    
    @GetMapping("/deals/{aptSeq}")
    public List<AptDeal> getDealsByAptSeq(@PathVariable("aptSeq") String aptSeq) {
        return aptService.getDealsByAptSeq(aptSeq);
    }
}
