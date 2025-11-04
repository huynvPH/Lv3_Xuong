package com.example.lv3.service;


import com.example.lv3.dto.request.ProductRequest;
import com.example.lv3.dto.response.ProductPageResponse;
import com.example.lv3.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
        ProductPageResponse searchProducts(String productName, Long brandId, Long categoryId, Long statusId, BigDecimal price, Pageable pageable);

	void createProduct(ProductRequest productRequest);

	void updateProduct(Long id, ProductRequest productRequest);

	void deleteProduct(Long id);

	ProductResponse getProduct(Long id);
}
