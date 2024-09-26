package com.milan.codechangepresentationgenerator.product.entity;

import com.milan.codechangepresentationgenerator.shared.abstractentity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "product_detail")
public class Product extends AbstractEntity {
    @Column(unique = true, nullable = false)
    private String productName;
    private double productPrice;
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;
    private String productDescription;
}
