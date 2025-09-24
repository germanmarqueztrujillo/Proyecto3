package com.example.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderStatusIsNotCreated extends RuntimeException {
  public OrderStatusIsNotCreated() {
    super("An order with a different status of created cannot be paid");
  }
}
