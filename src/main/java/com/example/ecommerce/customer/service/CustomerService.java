package com.example.ecommerce.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.repository.OrderRepository;

@Service
public class CustomerService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  
  public CustomerService (
    OrderRepository orderRepository,
    OrderMapper orderMapper
  ) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
    List<Order> orders = orderRepository.findByCustomerId(customerId);
    return orderMapper.toDtoList(orders);
  }
}
