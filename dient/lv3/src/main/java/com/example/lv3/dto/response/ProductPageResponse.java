package com.example.lv3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPageResponse {
	private List<ProductResponse> products;

	private int currentPage;

	private int totalPages;

	private long totalElements;
}
