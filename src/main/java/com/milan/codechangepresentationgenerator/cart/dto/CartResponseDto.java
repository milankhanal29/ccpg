package com.milan.codechangepresentationgenerator.cart.dto;

import com.milan.codechangepresentationgenerator.product.entity.Product;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDto {
    private int id;
    private int userId;
    private int productId;
    private Product product;
    private Status status;
}
