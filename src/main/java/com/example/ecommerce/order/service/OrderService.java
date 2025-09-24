package com.example.ecommerce.order.service;

import org.springframework.stereotype.Service;

import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.repository.ProductRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;

  public OrderService(
      OrderRepository orderRepository,
      CustomerRepository customerRepository,
      ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.productRepository = productRepository;
  }
  
}
