package com.milan.codechangepresentationgenerator.payment.repositories;

import com.milan.codechangepresentationgenerator.payment.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Integer> {
}
