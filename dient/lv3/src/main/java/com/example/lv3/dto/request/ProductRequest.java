package com.example.lv3.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
	private Long id;

	@NotBlank(message = "Product name must not be blank")
	private String productName;

	@NotBlank(message = "Color must not be blank")
	private String color;

	@NotNull(message = "Quantity must not be null")
	@Min(value = 0, message = "Quantity must be greater than or equal to 0")
	private Integer quantity;

	@NotNull(message = "Selling price must not be null")
	@Min(value = 0, message = "Selling price must be greater than or equal to 0")
	private BigDecimal sellPrice;

	@NotNull(message = "Origin price must not be null")
	@Min(value = 0, message = "Origin price must be greater than or equal to 0")
	private BigDecimal originPrice;

	private List<Long> brandId;

	private Long subcateId;

	private Long statusId;


}
