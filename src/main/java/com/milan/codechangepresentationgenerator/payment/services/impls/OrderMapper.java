package com.milan.codechangepresentationgenerator.payment.services.impls;
import com.milan.codechangepresentationgenerator.payment.order.Order;
import com.milan.codechangepresentationgenerator.payment.order.OrderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private ModelMapper modelMapper;
    @Autowired
    public OrderMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setUserId(orderDto.getUserId());
        return orderDto;
    }
    public Order mapToOrder(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }
}