package com.example.ecommerce.order.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.example.library.order")
public class OrderExceptionHandler {

  @ExceptionHandler(EmptyProductsException.class)
  public ResponseEntity<Map<String, Object>> handleEmptyProducts(EmptyProductsException ex) {
    return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleOrderNotFound(OrderNotFoundException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(OrderStatusIsNotCreatedException.class)
  public ResponseEntity<Map<String, Object>> handleOrderStatusIsNotCreated(
      OrderStatusIsNotCreatedException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(OrderStatusIsNotPaidException.class)
  public ResponseEntity<Map<String, Object>> handleOrderStatusIsNotPaid(
      OrderStatusIsNotPaidException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(OrderStatusIsNotShippedException.class)
  public ResponseEntity<Map<String, Object>> handleOrderStatusIsNotShipped(
      OrderStatusIsNotShippedException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("errors", errors);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(
      ConstraintViolationException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String fieldName = violation.getPropertyPath().toString();
              String errorMessage = violation.getMessage();
              errors.put(fieldName, errorMessage);
            });

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Validation Failed");
    body.put("errors", errors);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
      DataIntegrityViolationException ex) {
    String message =
        Optional.ofNullable(ex.getRootCause())
            .map(Throwable::getMessage)
            .orElse(Optional.ofNullable(ex.getMessage()).orElse("Data integrity violation"));

    return buildResponse(HttpStatus.BAD_REQUEST, message);
  }

  private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    return new ResponseEntity<>(body, status);
  }
}
