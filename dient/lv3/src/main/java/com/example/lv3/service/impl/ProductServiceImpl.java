package com.example.lv3.service.impl;


import com.example.lv3.dto.request.ProductRequest;
import com.example.lv3.dto.response.ProductPageResponse;
import com.example.lv3.dto.response.ProductResponse;
import com.example.lv3.model.Product;
import com.example.lv3.model.Brand;
import com.example.lv3.repository.*;
import com.example.lv3.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final CategoryRepository categoryRepository;

	private final BrandRepository brandRepository;

	private final SubCategoryRepository subCategoryRepository;

	private final StatusRepository statusRepository;

	@Override
	public ProductPageResponse searchProducts(String productName, Long brandId, Long categoryId, Long statusId, Float price, Pageable pageable) {
		// Kiểm tra price và thêm điều kiện lọc nếu price không null
		Page<ProductResponse> productPage;
		if (price != null) {
			productPage = productRepository.searchProducts(
					productName, brandId, categoryId, statusId, price.floatValue(), pageable
			);
		} else {
			productPage = productRepository.searchProducts(
					productName, brandId, categoryId, statusId, null, pageable
			);
		}

		return ProductPageResponse.builder()
				.products(productPage.getContent())
				.currentPage(productPage.getNumber())
				.totalPages(productPage.getTotalPages())
				.totalElements(productPage.getTotalElements())
				.build();
	}

	@Override
	public void createProduct(ProductRequest productRequest) {
		Product product = new Product();
		product.setProductName(productRequest.getProductName());
		product.setColor(productRequest.getColor());
		product.setQuantity(productRequest.getQuantity());
		product.setSellPrice(productRequest.getSellPrice());
		product.setOriginalPrice(productRequest.getOriginPrice());
		product.setBrands(productRequest.getBrandId() != null && !productRequest.getBrandId().isEmpty() ?
				brandRepository.findAllByIdIn(productRequest.getBrandId()) : List.of());
		// Kiểm tra subcateId
		if (productRequest.getSubcateId() != null) {
			product.setSubCategory(subCategoryRepository.findById(Math.toIntExact(productRequest.getSubcateId()))
					.orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + productRequest.getSubcateId())));
		}
		productRepository.save(product);
	}

	@Override
	public void updateProduct(Long id, ProductRequest productRequest) {

		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found"));


		existingProduct.setProductName(productRequest.getProductName());

		existingProduct.setColor(productRequest.getColor());

		existingProduct.setQuantity(productRequest.getQuantity());

		existingProduct.setSellPrice(productRequest.getSellPrice());

		existingProduct.setOriginalPrice(productRequest.getOriginPrice());

		existingProduct.setBrands(productRequest.getBrandId() != null && !productRequest.getBrandId().isEmpty() ?
				brandRepository.findAllByIdIn(productRequest.getBrandId()) : existingProduct.getBrands());
		// Kiểm tra subcateId
		if (productRequest.getSubcateId() != null) {
			existingProduct.setSubCategory(subCategoryRepository.findById(Math.toIntExact(productRequest.getSubcateId()))
					.orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + productRequest.getSubcateId())));
		}
		// Kiểm tra statusId
		if (productRequest.getStatusId() != null) {
			existingProduct.setStatus(statusRepository.findById(Math.toIntExact(productRequest.getStatusId()))
					.orElseThrow(() -> new RuntimeException("Status not found with ID: " + productRequest.getStatusId())));
		}

		productRepository.save(existingProduct);

	}

	@Override
	public void deleteProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		productRepository.delete(product);
	}

	@Override
	public ProductResponse getProduct(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		List<Long> brandIds = product.getBrands().stream().map(Brand::getId).collect(Collectors.toList());

		// Kiểm tra null cho product.getStatus()
		String statusName = product.getStatus() != null ? product.getStatus().getStatusName() : "";

		return ProductResponse.builder()
				.id(product.getId())
				.productName(product.getProductName())
				.color(product.getColor())
				.quantity(product.getQuantity())
				.sellPrice(product.getSellPrice())
				.originPrice(product.getOriginalPrice())
				.brandNames(product.getBrands().isEmpty() ? "" : product.getBrands().get(0).getBrandName())
				.subcateName(product.getSubCategory().getSubCateName())
				.statusName(statusName)
				.build();
	}
}
