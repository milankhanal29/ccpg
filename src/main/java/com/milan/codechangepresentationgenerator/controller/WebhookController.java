package com.milan.codechangepresentationgenerator.controller;

import com.milan.codechangepresentationgenerator.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;

    @PostMapping(value = "/github/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleGitHubWebhook(
            @PathVariable String email,
            @RequestBody Map<String, Object> payload) throws IOException {
        System.out.println("Received GitHub webhook with payload: " + payload);
        webhookService.processWebhook(payload, email);
        return new ResponseEntity<>("Webhook received", HttpStatus.OK);
    }
}

