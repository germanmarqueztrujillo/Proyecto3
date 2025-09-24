package com.example.ecommerce.order.service;

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
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final OrderMapper orderMapper;

  public OrderService(
      OrderRepository orderRepository,
      CustomerRepository customerRepository,
      ProductRepository productRepository,
      OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.productRepository = productRepository;
    this.orderMapper = orderMapper;
  }

  public OrderDTO getOrderById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;
    return orderMapper.toDTO(order);
  }

  public OrderDTO createOrder(OrderCreateDTO orderCreateDto) {
    if (orderCreateDto.getProductsId().size() == 0) {
      throw new EmptyProductsException();
    }

    Order order = orderMapper.toEntity(orderCreateDto);

    Customer customer =
        customerRepository
            .findById(orderCreateDto.getCustomerId())
            .orElseThrow(() -> new CustomerNotFoundException(orderCreateDto.getCustomerId()));
    ;
    List<Product> products = productRepository.findAllById(orderCreateDto.getProductsId());

    order.setCustomer(customer);
    order.setProducts(products);

    Order createdOrder = orderRepository.save(order);
    return orderMapper.toDTO(createdOrder);
  }

  public void updateStatusToPaidById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;

    if (order.getProducts().size() == 0) {
      throw new EmptyProductsException();
    } else if (order.getStatus() != Status.CREATED) {
      throw new OrderStatusIsNotCreatedException();
    }

    orderRepository.updateOrderStatusToPaidById(orderId);
  }

  public void updateStatusToShippedById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;

    if (order.getStatus() != Status.PAID) {
      throw new OrderStatusIsNotPaidException();
    }

    orderRepository.updateOrderStatusToShippedById(orderId);
  }

  public void updateStatusToDeliveredById(Long orderId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    ;

    if (order.getStatus() != Status.SHIPPED) {
      throw new OrderStatusIsNotShippedException();
    }

    orderRepository.updateOrderStatusToDeliveredById(orderId);
  }
}
