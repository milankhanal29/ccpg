package com.milan.codechangepresentationgenerator.payment.controllers;

import com.milan.codechangepresentationgenerator.payment.order.OrderDto;
import com.milan.codechangepresentationgenerator.payment.services.OrderService;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/subscription/{userId}")
    public List<OrderDto> getOrdersForUser(@PathVariable int userId) {
        return orderService.getOrdersForUser(userId);
    }
    @GetMapping("get-all-orders")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("get-by-id/{orderId}")
    public OrderDto getOrderById(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/save-order")
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        return orderService.createOrderWithDetails(orderDto);
    }

    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable int orderId, @RequestBody Status newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return "Order status updated successfully";
    }

}
