package com.example.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderStatusIsNotShippedException extends RuntimeException {
  public OrderStatusIsNotShippedException() {
    super("An order with a different status of shipped cannot be delivered");
  }
}
