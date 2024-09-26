package com.milan.codechangepresentationgenerator.payment.order;

import com.milan.codechangepresentationgenerator.shared.status.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    private int id;
    private int userId;
    private double totalPrice;
    private Status status;
    private String paymentStatus;
    private String transactionCode;
    private List<OrderDetailsDto> orderDetailsList;
}
