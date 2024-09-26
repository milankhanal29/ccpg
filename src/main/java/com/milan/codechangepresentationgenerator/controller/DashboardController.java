package com.milan.codechangepresentationgenerator.controller;

import com.milan.codechangepresentationgenerator.service.shared.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("/webhook-url/{orderId}")
    public ResponseEntity<Map<String, String>> getWebhookUrl(@PathVariable int orderId) {
        try {
            String webhookUrl = dashboardService.generateWebhookUrl(orderId);
            Map<String, String> response = new HashMap<>();
            response.put("url", webhookUrl);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("error", "Error generating webhook URL"), HttpStatus.INTERNAL_SERVER_ERROR);        }
    }
}
