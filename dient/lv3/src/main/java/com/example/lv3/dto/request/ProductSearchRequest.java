package com.example.lv3.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchRequest {
	private String productName;

	private BigDecimal price;

	private Long brandId;

	private Long categoryId;

	private Long statusId;

	private int page = 0;

	private int size = 5;

	
}
