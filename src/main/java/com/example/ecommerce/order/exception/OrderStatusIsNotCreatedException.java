package com.example.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderStatusIsNotCreatedException extends RuntimeException {
  public OrderStatusIsNotCreatedException() {
    super("An order with a different status of created cannot be paid");
  }
}
