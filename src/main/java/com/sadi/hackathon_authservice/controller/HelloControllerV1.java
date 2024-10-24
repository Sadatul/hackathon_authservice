package com.sadi.hackathon_authservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hello")
@Slf4j
public class HelloControllerV1 {

    @GetMapping("/healthy")
    public ResponseEntity<String> healthCheck(){
        log.info("Health check started");
        return ResponseEntity.ok("Hello World prod3");
    }
}
