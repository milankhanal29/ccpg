package com.milan.codechangepresentationgenerator.payment.services;

import com.milan.codechangepresentationgenerator.payment.order.OrderDto;
import com.milan.codechangepresentationgenerator.shared.status.Status;

import java.util.List;
public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(int orderId);
    OrderDto createOrderWithDetails(OrderDto orderDto);
    void updateOrderStatus(int orderId, Status newStatus);
    List<OrderDto> getOrdersForUser(int userId);
}
