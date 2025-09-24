package com.example.ecommerce.order.mapper;

import com.example.ecommerce.order.dto.OrderCreateDTO;
import com.example.ecommerce.order.dto.OrderDTO;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.product.model.Product;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "customer", ignore = true)
  @Mapping(target = "products", ignore = true)
  Order toEntity(OrderCreateDTO dto);

  @Mapping(source = "customer.id", target = "customerId")
  @Mapping(source = "products", target = "productsId")
  OrderDTO toDTO(Order order);

  default List<Long> mapProductsToIds(List<Product> products) {
    if (products == null) {
        return null;
    }
    return products.stream()
            .map(Product::getId)
            .toList();
  }
}