package com.example.ecommerce.order.dto;

import com.example.ecommerce.order.model.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
  @NotNull @Past private OffsetDateTime createdAt;

  private Status status;

  @NotNull private Long customerId;

  @NotNull private List<Long> productsId;
}
