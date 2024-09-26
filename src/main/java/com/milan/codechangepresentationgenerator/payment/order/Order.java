package com.milan.codechangepresentationgenerator.payment.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.milan.codechangepresentationgenerator.shared.abstractentity.AbstractEntity;
import com.milan.codechangepresentationgenerator.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
    private String paymentStatus;
    private String message;


    private double totalPrice;
    @Column(name = "encrypted_payload")
    private String encryptedPayload;
    @Column(unique = true, nullable = false)
    private String transactionCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetailsList = new ArrayList<>();
}