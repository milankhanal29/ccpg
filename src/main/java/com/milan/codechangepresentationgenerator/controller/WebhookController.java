package com.milan.codechangepresentationgenerator.controller;

import com.milan.codechangepresentationgenerator.service.WebhookService;
import com.milan.codechangepresentationgenerator.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping(value = "/github/{encryptedPayload}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleGitHubWebhook(
            @PathVariable String encryptedPayload,
            @RequestBody Map<String, Object> payload) throws Exception {

        String decryptedData = EncryptionUtil.decrypt(encryptedPayload);
        String[] parts = decryptedData.split(":");

        String email = parts[0];
        String transactionCode = parts[1];

        LocalDateTime expiryDate;
        try {
            // Attempt to parse with the full format first
            expiryDate = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (DateTimeParseException e) {
            try {
                // If it fails, attempt to parse with a more lenient format
                expiryDate = LocalDateTime.parse(parts[2], new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd'T'HH")
                        .optionalStart()
                        .appendLiteral(':')
                        .appendPattern("mm")
                        .optionalEnd()
                        .optionalStart()
                        .appendLiteral(':')
                        .appendPattern("ss")
                        .optionalEnd()
                        .toFormatter());
            } catch (DateTimeParseException e1) {
                log.error("Invalid expiry date format: {}", parts[2]);
                return new ResponseEntity<>("Invalid expiry date format", HttpStatus.BAD_REQUEST);
            }
        }

        // Check if the expiry date has passed
        if (LocalDateTime.now().isAfter(expiryDate)) {
            return new ResponseEntity<>("Webhook URL has expired", HttpStatus.FORBIDDEN);
        }

        log.debug("Processing webhook for email: {}, payload: {}", email, payload);
        webhookService.processWebhook(payload, email);
        return new ResponseEntity<>("Webhook processed successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/github/{encryptedPayload}")
    public ResponseEntity<String> getDecryptedPayload(@PathVariable String encryptedPayload) {
        try {
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload);
            log.info("Decrypted payload: {}", decryptedData);
            return new ResponseEntity<>("Decrypted data: " + decryptedData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error decrypting payload: {}", e.getMessage());
            return new ResponseEntity<>("Failed to decrypt payload", HttpStatus.BAD_REQUEST);
        }
    }
}
