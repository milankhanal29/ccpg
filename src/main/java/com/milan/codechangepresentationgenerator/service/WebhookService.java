package com.milan.codechangepresentationgenerator.service;

import java.io.IOException;
import java.util.Map;

public interface WebhookService{
    void processWebhook(Map<String, Object> payload) throws IOException;
}
