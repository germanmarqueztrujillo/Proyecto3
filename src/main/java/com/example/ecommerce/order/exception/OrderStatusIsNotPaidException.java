package com.example.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderStatusIsNotPaidException extends RuntimeException {
  public OrderStatusIsNotPaidException() {
    super("An order with a different status of paid cannot be shipped");
  }
}
