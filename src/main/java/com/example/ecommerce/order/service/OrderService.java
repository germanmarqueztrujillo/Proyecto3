package com.example.ecommerce.order.service;

import org.springframework.stereotype.Service;

import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.order.dto.OrderCreateDTO;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.exception.EmptyProductsException;
import com.example.ecommerce.order.exception.OrderNotFoundException;
import com.example.ecommerce.order.exception.OrderStatusIsNotCreated;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.Status;
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
  
  public OrderDTO createOrder(OrderCreateDTO orderCreateDto) {
    if (orderCreateDto.getProductsId().size() == 0) {
      throw new EmptyProductsException();
    }
    Order createdOrder = orderRepository.save(orderMapper.toEntity(orderCreateDto));
    return orderMapper.toDTO(createdOrder);
  }

  public void updateStatusToPaid(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));;

    if (order.getProducts().size() == 0) {
      throw new EmptyProductsException();
    } else if (order.getStatus() != Status.CREATED) {
      throw new OrderStatusIsNotCreated();
    }

    orderRepository.updateOrderStatusToPaidById(orderId);
  }

  public void updateStatusToShipped(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));;

    if (order.getStatus() != Status.PAID) {
      throw new OrderStatusIsNotCreated();
    }

    orderRepository.updateOrderStatusToShippedById(orderId);
  }
}
