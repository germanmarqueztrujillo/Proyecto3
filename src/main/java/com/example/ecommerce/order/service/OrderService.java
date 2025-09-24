package com.example.ecommerce.order.service;

import com.example.ecommerce.order.dto.OrderCreateDTO;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.exception.EmptyProductsException;
import com.example.ecommerce.order.exception.OrderNotFoundException;
import com.example.ecommerce.order.exception.OrderStatusIsNotCreated;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.Status;
import com.example.ecommerce.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public OrderService(
      OrderRepository orderRepository,
      OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  public OrderDTO getOrderById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;
    return orderMapper.toDTO(order);
  }

  public OrderDTO createOrder(OrderCreateDTO orderCreateDto) {
    if (orderCreateDto.getProductsId().size() == 0) {
      throw new EmptyProductsException();
    }
    Order createdOrder = orderRepository.save(orderMapper.toEntity(orderCreateDto));
    return orderMapper.toDTO(createdOrder);
  }

  public void updateStatusToPaidById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;

    if (order.getProducts().size() == 0) {
      throw new EmptyProductsException();
    } else if (order.getStatus() != Status.CREATED) {
      throw new OrderStatusIsNotCreated();
    }

    orderRepository.updateOrderStatusToPaidById(orderId);
  }

  public void updateStatusToShippedById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;

    if (order.getStatus() != Status.PAID) {
      throw new OrderStatusIsNotCreated();
    }

    orderRepository.updateOrderStatusToShippedById(orderId);
  }

  public void updateStatusToDeliveredById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;

    if (order.getStatus() != Status.SHIPPED) {
      throw new OrderStatusIsNotCreated();
    }

    orderRepository.updateOrderStatusToDeliveredById(orderId);
  }
}
