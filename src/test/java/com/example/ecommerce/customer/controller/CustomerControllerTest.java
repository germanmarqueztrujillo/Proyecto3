package com.example.ecommerce.customer.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerce.customer.service.CustomerService;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.model.Status;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private CustomerService customerService;

  private List<OrderDTO> sampleOrderDTOs;

  @TestConfiguration
  static class TestConfig {
    @Bean
    public CustomerService customerService() {
      return mock(CustomerService.class);
    }
  }

  @BeforeEach
  void setUp() {
    reset(customerService);
    
    OrderDTO orderDTO1 = new OrderDTO();
    orderDTO1.setCreatedAt(OffsetDateTime.now());
    orderDTO1.setStatus(Status.CREATED);
    orderDTO1.setCustomerId(1L);
    orderDTO1.setProductsId(Arrays.asList(1L, 2L));

    OrderDTO orderDTO2 = new OrderDTO();
    orderDTO2.setCreatedAt(OffsetDateTime.now().minusDays(1));
    orderDTO2.setStatus(Status.PAID);
    orderDTO2.setCustomerId(1L);
    orderDTO2.setProductsId(Arrays.asList(3L));

    sampleOrderDTOs = Arrays.asList(orderDTO1, orderDTO2);
  }

  @Test
  void whenGetOrdersByCustomerId_withExistingCustomer_thenReturnOrdersList() throws Exception {
    when(customerService.getOrdersByCustomerId(1L)).thenReturn(sampleOrderDTOs);

    mockMvc
        .perform(get("/customers/1/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].status").value("CREATED"))
        .andExpect(jsonPath("$[0].customerId").value(1))
        .andExpect(jsonPath("$[0].productsId.length()").value(2))
        .andExpect(jsonPath("$[0].productsId[0]").value(1))
        .andExpect(jsonPath("$[0].productsId[1]").value(2))
        .andExpect(jsonPath("$[1].status").value("PAID"))
        .andExpect(jsonPath("$[1].customerId").value(1))
        .andExpect(jsonPath("$[1].productsId.length()").value(1))
        .andExpect(jsonPath("$[1].productsId[0]").value(3));

    verify(customerService).getOrdersByCustomerId(1L);
  }

  @Test
  void whenGetOrdersByCustomerId_withCustomerWithoutOrders_thenReturnEmptyList() throws Exception {
    List<OrderDTO> emptyList = new ArrayList<>();
    when(customerService.getOrdersByCustomerId(2L)).thenReturn(emptyList);

    mockMvc
        .perform(get("/customers/2/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));

    verify(customerService).getOrdersByCustomerId(2L);
  }

  @Test
  void whenGetOrdersByCustomerId_withSingleOrder_thenReturnSingleOrderInList() throws Exception {
    List<OrderDTO> singleOrderList = Arrays.asList(sampleOrderDTOs.get(0));
    when(customerService.getOrdersByCustomerId(3L)).thenReturn(singleOrderList);

    mockMvc
        .perform(get("/customers/3/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].status").value("CREATED"))
        .andExpect(jsonPath("$[0].customerId").value(1))
        .andExpect(jsonPath("$[0].productsId.length()").value(2));

    verify(customerService).getOrdersByCustomerId(3L);
  }

  @Test
  void whenGetOrdersByCustomerId_withMultipleOrdersAndDifferentStatuses_thenReturnAllOrders() throws Exception {
    OrderDTO orderDTO3 = new OrderDTO();
    orderDTO3.setCreatedAt(OffsetDateTime.now().minusDays(2));
    orderDTO3.setStatus(Status.SHIPPED);
    orderDTO3.setCustomerId(1L);
    orderDTO3.setProductsId(Arrays.asList(4L, 5L));

    OrderDTO orderDTO4 = new OrderDTO();
    orderDTO4.setCreatedAt(OffsetDateTime.now().minusDays(3));
    orderDTO4.setStatus(Status.DELIVERED);
    orderDTO4.setCustomerId(1L);
    orderDTO4.setProductsId(Arrays.asList(6L));

    List<OrderDTO> multipleOrders = Arrays.asList(sampleOrderDTOs.get(0), sampleOrderDTOs.get(1), orderDTO3, orderDTO4);
    when(customerService.getOrdersByCustomerId(1L)).thenReturn(multipleOrders);

    mockMvc
        .perform(get("/customers/1/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(4))
        .andExpect(jsonPath("$[0].status").value("CREATED"))
        .andExpect(jsonPath("$[1].status").value("PAID"))
        .andExpect(jsonPath("$[2].status").value("SHIPPED"))
        .andExpect(jsonPath("$[2].productsId.length()").value(2))
        .andExpect(jsonPath("$[2].productsId[0]").value(4))
        .andExpect(jsonPath("$[2].productsId[1]").value(5))
        .andExpect(jsonPath("$[3].status").value("DELIVERED"))
        .andExpect(jsonPath("$[3].productsId.length()").value(1))
        .andExpect(jsonPath("$[3].productsId[0]").value(6));

    verify(customerService).getOrdersByCustomerId(1L);
  }

  @Test
  void whenGetOrdersByCustomerId_withInvalidPathVariable_thenHandleGracefully() throws Exception {
    when(customerService.getOrdersByCustomerId(999L)).thenReturn(new ArrayList<>());

    mockMvc
        .perform(get("/customers/999/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));

    verify(customerService).getOrdersByCustomerId(999L);
  }

  @Test
  void whenGetOrdersByCustomerId_verifyCorrectServiceInteraction() throws Exception {
    when(customerService.getOrdersByCustomerId(1L)).thenReturn(sampleOrderDTOs);

    mockMvc
        .perform(get("/customers/1/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(customerService, times(1)).getOrdersByCustomerId(1L);
    verifyNoMoreInteractions(customerService);
  }

  @Test
  void whenGetOrdersByCustomerId_withDifferentCustomerIds_thenCallServiceWithCorrectId() throws Exception {
    when(customerService.getOrdersByCustomerId(5L)).thenReturn(new ArrayList<>());
    when(customerService.getOrdersByCustomerId(10L)).thenReturn(sampleOrderDTOs);

    mockMvc
        .perform(get("/customers/5/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/customers/10/orders").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(customerService).getOrdersByCustomerId(5L);
    verify(customerService).getOrdersByCustomerId(10L);
  }
}
