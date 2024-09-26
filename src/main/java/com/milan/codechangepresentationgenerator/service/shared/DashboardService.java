package com.milan.codechangepresentationgenerator.service.shared;

import com.milan.codechangepresentationgenerator.payment.order.Order;
import com.milan.codechangepresentationgenerator.payment.repositories.OrderRepository;
import com.milan.codechangepresentationgenerator.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DashboardService {
    @Value("${webhook.payload.url}")
    private String payloadUrl;

    private final OrderRepository orderRepository;

    public String generateWebhookUrl(int orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Check if the encrypted payload already exists
        if (order.getEncryptedPayload() != null) {
            return payloadUrl + order.getEncryptedPayload(); // Return the existing payload
        }

        String email = order.getUser().getEmail();
        String transactionCode = order.getTransactionCode();

        LocalDateTime expiryDate = LocalDateTime.now().plusDays(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String payloadData = email + ":" + transactionCode + ":" + expiryDate.format(formatter);

        String encryptedPayload = EncryptionUtil.encrypt(payloadData);
        order.setEncryptedPayload(encryptedPayload);
        orderRepository.save(order);

        return payloadUrl + encryptedPayload;
    }
}
