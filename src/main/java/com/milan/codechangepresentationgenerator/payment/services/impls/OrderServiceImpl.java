package com.milan.codechangepresentationgenerator.payment.services.impls;

import com.milan.codechangepresentationgenerator.mapper.ProductModelMapper;
import com.milan.codechangepresentationgenerator.payment.order.Order;
import com.milan.codechangepresentationgenerator.payment.order.OrderDetails;
import com.milan.codechangepresentationgenerator.payment.order.OrderDetailsDto;
import com.milan.codechangepresentationgenerator.payment.order.OrderDto;
import com.milan.codechangepresentationgenerator.payment.repositories.OrderRepository;
import com.milan.codechangepresentationgenerator.payment.services.OrderService;
import com.milan.codechangepresentationgenerator.product.entity.Product;
import com.milan.codechangepresentationgenerator.product.service.ProductService;
import com.milan.codechangepresentationgenerator.service.NotificationService;
import com.milan.codechangepresentationgenerator.service.shared.MailService;
import com.milan.codechangepresentationgenerator.shared.exception.custom.ResourceNotFoundException;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderMapper orderMapper;
    private final ProductModelMapper productModelMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final MailService mailService;

    public OrderServiceImpl(MailService mailService,NotificationService notificationService,UserRepository userRepository,OrderRepository orderRepository, ProductService productService, OrderMapper orderMapper, ProductModelMapper productModelMapper) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderMapper = orderMapper;
        this.productModelMapper = productModelMapper;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mailService=mailService;

    }
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::mapToOrderDto)
                .toList();
    }
    @Override
    public OrderDto getOrderById(int orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(orderMapper::mapToOrderDto).orElse(null);
    }

    @Override
    public List<OrderDto> getOrdersForUser(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedDate).reversed())
                .map(orderMapper::mapToOrderDto)
                .toList();
    }
    @Override
    @Transactional
    public OrderDto createOrderWithDetails(OrderDto orderDto) {
        if (orderDto.getUserId() == 0) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Retrieve user from the database
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + orderDto.getUserId()));

        // Create the order
        Order order = orderMapper.mapToOrder(orderDto);
        order.setUser(user); // Assuming the Order has a reference to the user making the purchase

        // Get current date
        Date currentDate = new Date();

        if (orderDto.getOrderDetailsList() != null) {
            List<OrderDetails> orderDetailsList = new ArrayList<>();
            for (OrderDetailsDto orderDetailsDto : orderDto.getOrderDetailsList()) {
                OrderDetails orderDetails = new OrderDetails();
                Product product = productModelMapper.convertToEntity(
                        productService.getProductById(orderDetailsDto.getProductId()));
                orderDetails.setProduct(product);
                orderDetails.setOrder(order);
                orderDetails.setStatus(Status.PENDING);

                // Calculate expiry date as 30 days from now
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_MONTH, 30);
                Date expiryDate = calendar.getTime();

                orderDetails.setExpiryDate(expiryDate); // Set the expiry date
                orderDetailsList.add(orderDetails);
            }

            order.setOrderDetailsList(orderDetailsList);
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved successfully with ID: {}", savedOrder.getId());

        // Send confirmation email to the user
        mailService.sendEmail(user.getEmail(), "Order Confirmation",
                "<!DOCTYPE html><html><body>" +
                        "<h1>Your Order has been Placed!</h1>" +
                        "<p><strong>Order ID:</strong> " + savedOrder.getId() + "</p>" +
                        "<p><strong>Products:</strong></p>" +
                        "<ul>" +
                        savedOrder.getOrderDetailsList().stream()
                                .map(detail -> "<li>" + detail.getProduct().getProductName() + "</li>")
                                .collect(Collectors.joining()) +
                        "</ul>" +
                        "<p>Thank you for your purchase!</p>" +
                        "</body></html>");

        return orderMapper.mapToOrderDto(savedOrder);
    }


    @Override
    public void updateOrderStatus(int orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId));
        if (!Arrays.asList(Status.PENDING, Status.ACCEPTED).contains(newStatus)) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }
        order.setStatus(newStatus);
        orderRepository.save(order);
    }


}
