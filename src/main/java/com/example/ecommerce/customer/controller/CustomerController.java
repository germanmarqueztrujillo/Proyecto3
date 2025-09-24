package com.example.ecommerce.customer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.customer.service.CustomerService;
import com.example.ecommerce.order.dto.OrderDTO;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  
  private final CustomerService customerService;

  public CustomerController (
    CustomerService customerService
  ) {
    this.customerService = customerService;
  }

  @GetMapping("/{id}/orders")
  public List<OrderDTO> getOrdersByCustomerId(@PathVariable Long id) {
    return customerService.getOrdersByCustomerId(id);
  }
}
