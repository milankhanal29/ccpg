package com.milan.codechangepresentationgenerator.payment.repositories;

import com.milan.codechangepresentationgenerator.payment.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserId(int userId);
}
