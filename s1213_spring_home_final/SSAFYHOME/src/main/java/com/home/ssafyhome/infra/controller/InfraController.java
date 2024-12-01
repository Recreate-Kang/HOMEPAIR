package com.home.ssafyhome.infra.controller;

import com.home.ssafyhome.infra.dto.InfraListResponse;
import com.home.ssafyhome.infra.dto.RequestStore;
import com.home.ssafyhome.infra.service.InfraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("infra")
public class InfraController {

    InfraService infraService;

    InfraController(InfraService infraService) {
        this.infraService = infraService;
    }

    @PostMapping("list")
    ResponseEntity<InfraListResponse> getStoreListInRange(@RequestBody RequestStore request) {
        InfraListResponse response = infraService.getStoreListInRange(request);
        return ResponseEntity.ok().body(response);
    }
}
