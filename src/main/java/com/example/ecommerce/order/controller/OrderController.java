package com.example.ecommerce.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.order.dto.OrderCreateDTO;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {
    return orderService.createOrder(orderCreateDTO);
  }

  @GetMapping("/{id}")
  public OrderDTO getOrder(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  @PatchMapping("/{id}/pay")
  public void updateStatusToPaid(@PathVariable Long id) {
    orderService.updateStatusToPaidById(id);
  }

  @PatchMapping("/{id}/ship")
  public void updateStatusToShipped(@PathVariable Long id) {
    orderService.updateStatusToShippedById(id);
  }
}