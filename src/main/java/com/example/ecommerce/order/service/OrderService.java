package com.example.ecommerce.order.service;

import org.springframework.stereotype.Service;

import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.exception.EmptyProductsException;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.repository.ProductRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final OrderMapper orderMapper;

  public OrderService(
      OrderRepository orderRepository,
      CustomerRepository customerRepository,
      ProductRepository productRepository,
      OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.productRepository = productRepository;
    this.orderMapper = orderMapper;
  }
  
  public OrderDTO createOrder(OrderDTO orderDto) {
    if (orderDto.getProductsId().size() == 0) {
      throw new EmptyProductsException();
    }
    Order createdOrder = orderRepository.save(orderMapper.toEntity(orderDto));
    return orderMapper.toDTO(createdOrder);
  }
}
