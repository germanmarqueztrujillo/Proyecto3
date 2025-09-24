package com.example.ecommerce.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.ecommerce.customer.model.Customer;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.Status;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.model.Product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock private OrderRepository orderRepository;
  @Mock private OrderMapper orderMapper;

  @InjectMocks private CustomerService customerService;

  private Customer sampleCustomer;
  private List<Order> sampleOrders;
  private List<OrderDTO> sampleOrderDTOs;
  private Product sampleProduct;

  @BeforeEach
  void setUp() {
    sampleCustomer = new Customer();
    sampleCustomer.setId(1L);
    sampleCustomer.setName("John Doe");
    sampleCustomer.setEmail("john@example.com");

    sampleProduct = new Product();
    sampleProduct.setId(1L);
    sampleProduct.setName("Sample Product");
    sampleProduct.setPrice(new BigDecimal("99.99"));

    Order order1 = new Order();
    order1.setId(1L);
    order1.setStatus(Status.CREATED);
    order1.setCreatedAt(OffsetDateTime.now());
    order1.setCustomer(sampleCustomer);
    order1.setProducts(Arrays.asList(sampleProduct));

    Order order2 = new Order();
    order2.setId(2L);
    order2.setStatus(Status.PAID);
    order2.setCreatedAt(OffsetDateTime.now().minusDays(1));
    order2.setCustomer(sampleCustomer);
    order2.setProducts(Arrays.asList(sampleProduct));

    sampleOrders = Arrays.asList(order1, order2);

    OrderDTO orderDTO1 = new OrderDTO();
    orderDTO1.setCreatedAt(order1.getCreatedAt());
    orderDTO1.setStatus(Status.CREATED);
    orderDTO1.setCustomerId(1L);
    orderDTO1.setProductsId(Arrays.asList(1L));

    OrderDTO orderDTO2 = new OrderDTO();
    orderDTO2.setCreatedAt(order2.getCreatedAt());
    orderDTO2.setStatus(Status.PAID);
    orderDTO2.setCustomerId(1L);
    orderDTO2.setProductsId(Arrays.asList(1L));

    sampleOrderDTOs = Arrays.asList(orderDTO1, orderDTO2);
  }

  @Test
  void whenGetOrdersByCustomerId_withExistingCustomer_thenReturnOrderDTOList() {
    when(orderRepository.findByCustomerId(1L)).thenReturn(sampleOrders);
    when(orderMapper.toDtoList(sampleOrders)).thenReturn(sampleOrderDTOs);

    List<OrderDTO> result = customerService.getOrdersByCustomerId(1L);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(Status.CREATED, result.get(0).getStatus());
    assertEquals(Status.PAID, result.get(1).getStatus());
    assertEquals(1L, result.get(0).getCustomerId());
    assertEquals(1L, result.get(1).getCustomerId());
    
    verify(orderRepository).findByCustomerId(1L);
    verify(orderMapper).toDtoList(sampleOrders);
  }

  @Test
  void whenGetOrdersByCustomerId_withCustomerWithoutOrders_thenReturnEmptyList() {
    List<Order> emptyOrders = new ArrayList<>();
    List<OrderDTO> emptyOrderDTOs = new ArrayList<>();
    
    when(orderRepository.findByCustomerId(2L)).thenReturn(emptyOrders);
    when(orderMapper.toDtoList(emptyOrders)).thenReturn(emptyOrderDTOs);

    List<OrderDTO> result = customerService.getOrdersByCustomerId(2L);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(0, result.size());
    
    verify(orderRepository).findByCustomerId(2L);
    verify(orderMapper).toDtoList(emptyOrders);
  }

  @Test
  void whenGetOrdersByCustomerId_withSingleOrder_thenReturnSingleOrderDTO() {
    List<Order> singleOrder = Arrays.asList(sampleOrders.get(0));
    List<OrderDTO> singleOrderDTO = Arrays.asList(sampleOrderDTOs.get(0));
    
    when(orderRepository.findByCustomerId(1L)).thenReturn(singleOrder);
    when(orderMapper.toDtoList(singleOrder)).thenReturn(singleOrderDTO);

    List<OrderDTO> result = customerService.getOrdersByCustomerId(1L);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(Status.CREATED, result.get(0).getStatus());
    assertEquals(1L, result.get(0).getCustomerId());
    assertNotNull(result.get(0).getProductsId());
    assertEquals(1, result.get(0).getProductsId().size());
    assertEquals(1L, result.get(0).getProductsId().get(0));
    
    verify(orderRepository).findByCustomerId(1L);
    verify(orderMapper).toDtoList(singleOrder);
  }

  @Test
  void whenGetOrdersByCustomerId_withMultipleOrdersAndDifferentStatuses_thenReturnAllOrderDTOs() {
    Order order3 = new Order();
    order3.setId(3L);
    order3.setStatus(Status.SHIPPED);
    order3.setCreatedAt(OffsetDateTime.now().minusDays(2));
    order3.setCustomer(sampleCustomer);
    order3.setProducts(Arrays.asList(sampleProduct));

    Order order4 = new Order();
    order4.setId(4L);
    order4.setStatus(Status.DELIVERED);
    order4.setCreatedAt(OffsetDateTime.now().minusDays(3));
    order4.setCustomer(sampleCustomer);
    order4.setProducts(Arrays.asList(sampleProduct));

    List<Order> multipleOrders = Arrays.asList(sampleOrders.get(0), sampleOrders.get(1), order3, order4);

    OrderDTO orderDTO3 = new OrderDTO();
    orderDTO3.setCreatedAt(order3.getCreatedAt());
    orderDTO3.setStatus(Status.SHIPPED);
    orderDTO3.setCustomerId(1L);
    orderDTO3.setProductsId(Arrays.asList(1L));

    OrderDTO orderDTO4 = new OrderDTO();
    orderDTO4.setCreatedAt(order4.getCreatedAt());
    orderDTO4.setStatus(Status.DELIVERED);
    orderDTO4.setCustomerId(1L);
    orderDTO4.setProductsId(Arrays.asList(1L));

    List<OrderDTO> multipleOrderDTOs = Arrays.asList(sampleOrderDTOs.get(0), sampleOrderDTOs.get(1), orderDTO3, orderDTO4);

    when(orderRepository.findByCustomerId(1L)).thenReturn(multipleOrders);
    when(orderMapper.toDtoList(multipleOrders)).thenReturn(multipleOrderDTOs);

    List<OrderDTO> result = customerService.getOrdersByCustomerId(1L);

    assertNotNull(result);
    assertEquals(4, result.size());
    assertEquals(Status.CREATED, result.get(0).getStatus());
    assertEquals(Status.PAID, result.get(1).getStatus());
    assertEquals(Status.SHIPPED, result.get(2).getStatus());
    assertEquals(Status.DELIVERED, result.get(3).getStatus());
    
    result.forEach(orderDTO -> {
      assertEquals(1L, orderDTO.getCustomerId());
      assertNotNull(orderDTO.getCreatedAt());
      assertNotNull(orderDTO.getProductsId());
      assertFalse(orderDTO.getProductsId().isEmpty());
    });
    
    verify(orderRepository).findByCustomerId(1L);
    verify(orderMapper).toDtoList(multipleOrders);
  }

  @Test
  void whenGetOrdersByCustomerId_verifyRepositoryAndMapperInteractions() {
    when(orderRepository.findByCustomerId(1L)).thenReturn(sampleOrders);
    when(orderMapper.toDtoList(sampleOrders)).thenReturn(sampleOrderDTOs);

    customerService.getOrdersByCustomerId(1L);

    verify(orderRepository, times(1)).findByCustomerId(1L);
    verify(orderMapper, times(1)).toDtoList(sampleOrders);
    verifyNoMoreInteractions(orderRepository, orderMapper);
  }
}