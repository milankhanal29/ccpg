package com.milan.codechangepresentationgenerator.dto;

import lombok.Data;

import java.util.Map;
@Data
public class WebhookPayload {
    private Map<String, Object> repository;
}
