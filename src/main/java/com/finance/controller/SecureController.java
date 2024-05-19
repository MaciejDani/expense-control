package com.finance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class SecureController {

    @GetMapping("/secure-endpoint")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("You have access to this endpoint");
    }
}