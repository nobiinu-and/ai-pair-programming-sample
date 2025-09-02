package com.example.aipairprogrammingsample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, World!");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
    
}
