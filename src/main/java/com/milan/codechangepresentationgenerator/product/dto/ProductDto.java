package com.milan.codechangepresentationgenerator.product.dto;

import com.milan.codechangepresentationgenerator.shared.status.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductDto {
    private int id;
    private String productName;
    private double productPrice;
    private byte[] productImage;
    private String productDescription;
    private Status status= Status.ACTIVE;
    private Date createdDate;
}
