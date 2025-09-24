package com.example.ecommerce.order.dto;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {
  @NotNull @Past private OffsetDateTime createdAt;

  @NotNull private Long customerId;

  @NotNull private List<Long> productsId;
}
