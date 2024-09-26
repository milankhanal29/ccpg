package com.milan.codechangepresentationgenerator.payment.order;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderDetailsDto {
    private int id;
    private int orderId;
    private int productId;
    private Date expiryDate;
}
