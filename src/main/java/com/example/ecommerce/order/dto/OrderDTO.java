package com.example.ecommerce.order.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.example.ecommerce.order.model.Status;

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
public class OrderDTO {
  @NotNull @Past private OffsetDateTime dueDate;
  private Status status;

  @NotNull private Long customerId;

  @NotNull private List<Long> productsId;
}
