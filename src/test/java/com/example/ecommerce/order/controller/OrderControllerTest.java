package com.example.ecommerce.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerce.order.dto.OrderCreateDTO;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.exception.CustomerNotFoundException;
import com.example.ecommerce.order.exception.EmptyProductsException;
import com.example.ecommerce.order.exception.OrderNotFoundException;
import com.example.ecommerce.order.exception.OrderStatusIsNotCreatedException;
import com.example.ecommerce.order.exception.OrderStatusIsNotPaidException;
import com.example.ecommerce.order.exception.OrderStatusIsNotShippedException;
import com.example.ecommerce.order.model.Status;
import com.example.ecommerce.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private OrderService orderService;

  @TestConfiguration
  static class MockConfig {
    @Bean
    OrderService orderService() {
      return Mockito.mock(OrderService.class);
    }
  }

  private OrderDTO sampleOrderDTO;
  private OrderCreateDTO sampleOrderCreateDTO;

  @BeforeEach
  void setUp() {
    Mockito.reset(orderService);

    sampleOrderCreateDTO = new OrderCreateDTO(1L, Arrays.asList(1L, 2L, 3L));

    sampleOrderDTO = new OrderDTO();
    sampleOrderDTO.setCreatedAt(OffsetDateTime.now().minusHours(1));
    sampleOrderDTO.setStatus(Status.CREATED);
    sampleOrderDTO.setCustomerId(1L);
    sampleOrderDTO.setProductsId(Arrays.asList(1L, 2L, 3L));
  }

  @Test
  void whenCreateValidRequest_thenReturnCreatedOrder() throws Exception {
    when(orderService.createOrder(any(OrderCreateDTO.class))).thenReturn(sampleOrderDTO);
    mockMvc
        .perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleOrderCreateDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(jsonPath("$.customerId").value(1L))
        .andExpect(jsonPath("$.productsId").isArray());

    verify(orderService).createOrder(any(OrderCreateDTO.class));
  }

  @Test
  void whenGetOrderWithValidId_thenReturnOrder() throws Exception {
    Long orderId = 1L;
    when(orderService.getOrderById(orderId)).thenReturn(sampleOrderDTO);
    mockMvc
        .perform(get("/orders/{id}", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(jsonPath("$.customerId").value(1L));

    verify(orderService).getOrderById(orderId);
  }

  @Test
  void whenGetOrderWithNonExistentId_thenReturn404() throws Exception {
    Long nonExistentId = 999L;
    when(orderService.getOrderById(nonExistentId))
        .thenThrow(new OrderNotFoundException(nonExistentId));
    mockMvc.perform(get("/orders/{id}", nonExistentId)).andExpect(status().isNotFound());
  }

  @Test
  void whenUpdateStatusToPaid_thenReturnOk() throws Exception {
    Long orderId = 1L;
    doNothing().when(orderService).updateStatusToPaidById(orderId);
    mockMvc.perform(patch("/orders/{id}/pay", orderId)).andExpect(status().isOk());

    verify(orderService).updateStatusToPaidById(orderId);
  }

  @Test
  void whenUpdateStatusToShipped_thenReturnOk() throws Exception {
    Long orderId = 1L;
    doNothing().when(orderService).updateStatusToShippedById(orderId);
    mockMvc.perform(patch("/orders/{id}/ship", orderId)).andExpect(status().isOk());

    verify(orderService).updateStatusToShippedById(orderId);
  }

  @Test
  void whenUpdateStatusToDelivered_thenReturnOk() throws Exception {
    Long orderId = 1L;
    doNothing().when(orderService).updateStatusToDeliveredById(orderId);
    mockMvc.perform(patch("/orders/{id}/deliver", orderId)).andExpect(status().isOk());

    verify(orderService).updateStatusToDeliveredById(orderId);
  }

  @Test
  void whenCreateOrderWithNonExistentCustomer_thenReturn404() throws Exception {
    when(orderService.createOrder(any(OrderCreateDTO.class)))
        .thenThrow(new CustomerNotFoundException(999L));

    OrderCreateDTO invalidRequest = new OrderCreateDTO(999L, Arrays.asList(1L, 2L));
    mockMvc
        .perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  void whenCreateOrderWithEmptyProducts_thenReturn409() throws Exception {
    when(orderService.createOrder(any(OrderCreateDTO.class)))
        .thenThrow(new EmptyProductsException());

    OrderCreateDTO emptyProductsRequest = new OrderCreateDTO(1L, new ArrayList<>());
    mockMvc
        .perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyProductsRequest)))
        .andExpect(status().isConflict());
  }

  @Test
  void whenUpdateStatusToPaidWithWrongStatus_thenReturn404() throws Exception {
    Long orderId = 1L;
    doThrow(new OrderStatusIsNotCreatedException())
        .when(orderService)
        .updateStatusToPaidById(orderId);
    mockMvc.perform(patch("/orders/{id}/pay", orderId)).andExpect(status().isConflict());
  }

  @Test
  void whenUpdateStatusToShippedWithWrongStatus_thenReturn404() throws Exception {
    Long orderId = 1L;
    doThrow(new OrderStatusIsNotPaidException())
        .when(orderService)
        .updateStatusToShippedById(orderId);
    mockMvc.perform(patch("/orders/{id}/ship", orderId)).andExpect(status().isConflict());
  }

  @Test
  void whenUpdateStatusToDeliveredWithWrongStatus_thenReturn404() throws Exception {
    Long orderId = 1L;
    doThrow(new OrderStatusIsNotShippedException())
        .when(orderService)
        .updateStatusToDeliveredById(orderId);
    mockMvc.perform(patch("/orders/{id}/deliver", orderId)).andExpect(status().isConflict());
  }
}
