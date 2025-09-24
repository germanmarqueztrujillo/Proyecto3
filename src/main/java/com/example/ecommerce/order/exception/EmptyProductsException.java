package com.example.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmptyProductsException extends RuntimeException {
  public EmptyProductsException() {
    super("The order dont have any product");
  }
}
