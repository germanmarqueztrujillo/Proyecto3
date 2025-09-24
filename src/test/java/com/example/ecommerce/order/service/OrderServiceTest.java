package com.example.ecommerce.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.example.ecommerce.customer.model.Customer;
import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.order.dto.OrderCreateDTO;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.exception.CustomerNotFoundException;
import com.example.ecommerce.order.exception.EmptyProductsException;
import com.example.ecommerce.order.exception.OrderNotFoundException;
import com.example.ecommerce.order.exception.OrderStatusIsNotCreatedException;
import com.example.ecommerce.order.exception.OrderStatusIsNotPaidException;
import com.example.ecommerce.order.exception.OrderStatusIsNotShippedException;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.Status;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock private OrderRepository orderRepository;
  @Mock private CustomerRepository customerRepository;
  @Mock private ProductRepository productRepository;
  @Mock private OrderMapper orderMapper;

  @InjectMocks private OrderService orderService;

  private Order sampleOrder;
  private OrderDTO sampleOrderDTO;
  private OrderCreateDTO sampleOrderCreateDTO;
  private Customer sampleCustomer;
  private List<Product> sampleProducts;

  @BeforeEach
  void setUp() {
    sampleCustomer = new Customer();
    sampleCustomer.setId(1L);
    sampleCustomer.setName("John Doe");
    sampleCustomer.setEmail("john@example.com");

    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Product 1");
    product1.setPrice(new BigDecimal("10.00"));

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Product 2");
    product2.setPrice(new BigDecimal("20.00"));

    sampleProducts = Arrays.asList(product1, product2);

    sampleOrder = new Order();
    sampleOrder.setId(1L);
    sampleOrder.setStatus(Status.CREATED);
    sampleOrder.setCreatedAt(OffsetDateTime.now());
    sampleOrder.setCustomer(sampleCustomer);
    sampleOrder.setProducts(sampleProducts);

    sampleOrderDTO = new OrderDTO();
    sampleOrderDTO.setCreatedAt(OffsetDateTime.now());
    sampleOrderDTO.setStatus(Status.CREATED);
    sampleOrderDTO.setCustomerId(1L);
    sampleOrderDTO.setProductsId(Arrays.asList(1L, 2L));

    sampleOrderCreateDTO = new OrderCreateDTO();
    sampleOrderCreateDTO.setCustomerId(1L);
    sampleOrderCreateDTO.setProductsId(Arrays.asList(1L, 2L));
  }

  @Test
  void whenGetOrderById_withValidId_thenReturnOrderDTO() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
    when(orderMapper.toDTO(sampleOrder)).thenReturn(sampleOrderDTO);

    OrderDTO result = orderService.getOrderById(1L);

    assertNotNull(result);
    assertEquals(sampleOrderDTO.getStatus(), result.getStatus());
    assertEquals(sampleOrderDTO.getCustomerId(), result.getCustomerId());
    verify(orderRepository).findById(1L);
    verify(orderMapper).toDTO(sampleOrder);
  }

  @Test
  void whenGetOrderById_withInvalidId_thenThrowOrderNotFoundException() {
    when(orderRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(999L));
    verify(orderRepository).findById(999L);
    verify(orderMapper, never()).toDTO(any());
  }

  @Test
  void whenCreateOrder_withValidData_thenReturnOrderDTO() {
    when(orderMapper.toEntity(sampleOrderCreateDTO)).thenReturn(sampleOrder);
    when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
    when(productRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(sampleProducts);
    when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);
    when(orderMapper.toDTO(sampleOrder)).thenReturn(sampleOrderDTO);

    OrderDTO result = orderService.createOrder(sampleOrderCreateDTO);

    assertNotNull(result);
    assertEquals(sampleOrderDTO.getStatus(), result.getStatus());
    assertEquals(sampleOrderDTO.getCustomerId(), result.getCustomerId());
    verify(orderMapper).toEntity(sampleOrderCreateDTO);
    verify(customerRepository).findById(1L);
    verify(productRepository).findAllById(Arrays.asList(1L, 2L));
    verify(orderRepository).save(any(Order.class));
    verify(orderMapper).toDTO(sampleOrder);
  }

  @Test
  void whenCreateOrder_withEmptyProducts_thenThrowEmptyProductsException() {
    OrderCreateDTO emptyProductsDTO = new OrderCreateDTO();
    emptyProductsDTO.setCustomerId(1L);
    emptyProductsDTO.setProductsId(new ArrayList<>());

    assertThrows(EmptyProductsException.class, () -> orderService.createOrder(emptyProductsDTO));
    verify(orderMapper, never()).toEntity(any());
    verify(customerRepository, never()).findById(any());
    verify(productRepository, never()).findAllById(anyList());
    verify(orderRepository, never()).save(any());
  }

  @Test
  void whenCreateOrder_withNonExistentCustomer_thenThrowCustomerNotFoundException() {
    when(orderMapper.toEntity(sampleOrderCreateDTO)).thenReturn(sampleOrder);
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        CustomerNotFoundException.class, () -> orderService.createOrder(sampleOrderCreateDTO));
    verify(orderMapper).toEntity(sampleOrderCreateDTO);
    verify(customerRepository).findById(1L);
    verify(productRepository, never()).findAllById(anyList());
    verify(orderRepository, never()).save(any());
  }

  @Test
  void whenUpdateStatusToPaid_withValidOrder_thenUpdateStatus() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

    orderService.updateStatusToPaidById(1L);

    verify(orderRepository).findById(1L);
    verify(orderRepository).updateOrderStatusToPaidById(1L);
  }

  @Test
  void whenUpdateStatusToPaid_withNonExistentOrder_thenThrowOrderNotFoundException() {
    when(orderRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(OrderNotFoundException.class, () -> orderService.updateStatusToPaidById(999L));
    verify(orderRepository).findById(999L);
    verify(orderRepository, never()).updateOrderStatusToPaidById(any());
  }

  @Test
  void whenUpdateStatusToPaid_withEmptyProducts_thenThrowEmptyProductsException() {
    Order orderWithoutProducts = new Order();
    orderWithoutProducts.setId(1L);
    orderWithoutProducts.setStatus(Status.CREATED);
    orderWithoutProducts.setProducts(new ArrayList<>());

    when(orderRepository.findById(1L)).thenReturn(Optional.of(orderWithoutProducts));

    assertThrows(EmptyProductsException.class, () -> orderService.updateStatusToPaidById(1L));
    verify(orderRepository).findById(1L);
    verify(orderRepository, never()).updateOrderStatusToPaidById(any());
  }

  @Test
  void whenUpdateStatusToPaid_withWrongStatus_thenThrowOrderStatusIsNotCreatedException() {
    Order paidOrder = new Order();
    paidOrder.setId(1L);
    paidOrder.setStatus(Status.PAID);
    paidOrder.setProducts(sampleProducts);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(paidOrder));

    assertThrows(
        OrderStatusIsNotCreatedException.class, () -> orderService.updateStatusToPaidById(1L));
    verify(orderRepository).findById(1L);
    verify(orderRepository, never()).updateOrderStatusToPaidById(any());
  }

  @Test
  void whenUpdateStatusToShipped_withValidOrder_thenUpdateStatus() {
    Order paidOrder = new Order();
    paidOrder.setId(1L);
    paidOrder.setStatus(Status.PAID);
    paidOrder.setProducts(sampleProducts);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(paidOrder));

    orderService.updateStatusToShippedById(1L);

    verify(orderRepository).findById(1L);
    verify(orderRepository).updateOrderStatusToShippedById(1L);
  }

  @Test
  void whenUpdateStatusToShipped_withNonExistentOrder_thenThrowOrderNotFoundException() {
    when(orderRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(OrderNotFoundException.class, () -> orderService.updateStatusToShippedById(999L));
    verify(orderRepository).findById(999L);
    verify(orderRepository, never()).updateOrderStatusToShippedById(any());
  }

  @Test
  void whenUpdateStatusToShipped_withWrongStatus_thenThrowOrderStatusIsNotPaidException() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

    assertThrows(
        OrderStatusIsNotPaidException.class, () -> orderService.updateStatusToShippedById(1L));
    verify(orderRepository).findById(1L);
    verify(orderRepository, never()).updateOrderStatusToShippedById(any());
  }

  @Test
  void whenUpdateStatusToDelivered_withValidOrder_thenUpdateStatus() {
    Order shippedOrder = new Order();
    shippedOrder.setId(1L);
    shippedOrder.setStatus(Status.SHIPPED);
    shippedOrder.setProducts(sampleProducts);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(shippedOrder));

    orderService.updateStatusToDeliveredById(1L);

    verify(orderRepository).findById(1L);
    verify(orderRepository).updateOrderStatusToDeliveredById(1L);
  }

  @Test
  void whenUpdateStatusToDelivered_withNonExistentOrder_thenThrowOrderNotFoundException() {
    when(orderRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        OrderNotFoundException.class, () -> orderService.updateStatusToDeliveredById(999L));
    verify(orderRepository).findById(999L);
    verify(orderRepository, never()).updateOrderStatusToDeliveredById(any());
  }

  @Test
  void whenUpdateStatusToDelivered_withWrongStatus_thenThrowOrderStatusIsNotShippedException() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

    assertThrows(
        OrderStatusIsNotShippedException.class, () -> orderService.updateStatusToDeliveredById(1L));
    verify(orderRepository).findById(1L);
    verify(orderRepository, never()).updateOrderStatusToDeliveredById(any());
  }
}
