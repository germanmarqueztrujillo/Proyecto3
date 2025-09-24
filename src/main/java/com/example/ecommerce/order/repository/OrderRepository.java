package com.example.ecommerce.order.repository;

import com.example.ecommerce.order.model.Order;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE Order o SET o.status = PAID WHERE o.id = :orderId")
  void updateOrderStatusToPaidById(@Param("orderId") Long orderId);

  @Modifying
  @Transactional
  @Query("UPDATE Order o SET o.status = SHIPPED WHERE o.id = :orderId")
  void updateOrderStatusToShippedById(@Param("orderId") Long orderId);

  @Modifying
  @Transactional
  @Query("UPDATE Order o SET o.status = DELIVERED WHERE o.id = :orderId")
  void updateOrderStatusToDeliveredById(@Param("orderId") Long orderId);

  List<Order> findByCustomerId(Long customerId);
}
