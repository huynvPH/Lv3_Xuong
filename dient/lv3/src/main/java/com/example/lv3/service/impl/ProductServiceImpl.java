package com.example.lv3.service.impl;


import com.example.lv3.dto.request.ProductRequest;
import com.example.lv3.dto.response.ProductPageResponse;
import com.example.lv3.dto.response.ProductResponse;
import com.example.lv3.model.Brand;
import com.example.lv3.model.Product;
import com.example.lv3.repository.*;
import com.example.lv3.repository.projection.ProductSearchProjection;
import com.example.lv3.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

        private final BrandRepository brandRepository;

	private final SubCategoryRepository subCategoryRepository;

	private final StatusRepository statusRepository;

	@Override
        public ProductPageResponse searchProducts(String productName, Long brandId, Long categoryId, Long statusId, BigDecimal price, Pageable pageable) {
                Page<ProductSearchProjection> productPage = productRepository.searchProducts(
                                productName, brandId, categoryId, statusId, price, pageable
                );

                Page<ProductResponse> responsePage = productPage.map(this::mapToProductResponse);

                return ProductPageResponse.builder()
                                .products(responsePage.getContent())
                                .currentPage(responsePage.getNumber())
                                .totalPages(responsePage.getTotalPages())
                                .totalElements(responsePage.getTotalElements())
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
                product.setBrands(getBrands(productRequest));
                // Kiểm tra subcateId
                if (productRequest.getSubcateId() != null) {
                        product.setSubCategory(subCategoryRepository.findById(productRequest.getSubcateId())
                                        .orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + productRequest.getSubcateId())));
                }
                if (productRequest.getStatusId() != null) {
                        product.setStatus(statusRepository.findById(productRequest.getStatusId())
                                        .orElseThrow(() -> new RuntimeException("Status not found with ID: " + productRequest.getStatusId())));
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

                if (productRequest.getBrandIds() != null) {
                        existingProduct.setBrands(productRequest.getBrandIds().isEmpty()
                                        ? Collections.emptyList()
                                        : brandRepository.findAllByIdIn(productRequest.getBrandIds()));
                }
                // Kiểm tra subcateId
                if (productRequest.getSubcateId() != null) {
                        existingProduct.setSubCategory(subCategoryRepository.findById(productRequest.getSubcateId())
                                        .orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + productRequest.getSubcateId())));
                }
                // Kiểm tra statusId
                if (productRequest.getStatusId() != null) {
                        existingProduct.setStatus(statusRepository.findById(productRequest.getStatusId())
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
                List<Brand> brands = product.getBrands() != null ? product.getBrands() : Collections.emptyList();
                List<Long> brandIds = brands.stream().map(Brand::getId).collect(Collectors.toList());
                String brandNames = brands.stream().map(Brand::getBrandName).collect(Collectors.joining(", "));

                String statusName = product.getStatus() != null ? product.getStatus().getStatusName() : "";
                Long statusId = product.getStatus() != null ? product.getStatus().getId() : null;

                Long subcateId = product.getSubCategory() != null ? product.getSubCategory().getId() : null;
                String subcateName = product.getSubCategory() != null ? product.getSubCategory().getSubCateName() : "";

                return ProductResponse.builder()
                                .id(product.getId())
                                .productName(product.getProductName())
                                .color(product.getColor())
                                .quantity(product.getQuantity())
                                .sellPrice(product.getSellPrice())
                                .originPrice(product.getOriginalPrice())
                                .brandNames(brandNames)
                                .brandIds(brandIds)
                                .subcateId(subcateId)
                                .subcateName(subcateName)
                                .statusId(statusId)
                                .statusName(statusName)
                                .build();
        }

        private List<Brand> getBrands(ProductRequest productRequest) {
                List<Long> brandIds = productRequest.getBrandIds();
                if (brandIds == null || brandIds.isEmpty()) {
                        return Collections.emptyList();
                }
                return brandRepository.findAllByIdIn(brandIds);
        }

        private ProductResponse mapToProductResponse(ProductSearchProjection projection) {
                return ProductResponse.builder()
                                .id(projection.getId())
                                .productName(projection.getProductName())
                                .color(projection.getColor())
                                .quantity(projection.getQuantity())
                                .sellPrice(projection.getSellPrice())
                                .originPrice(projection.getOriginPrice())
                                .statusName(projection.getStatusName() != null ? projection.getStatusName() : "")
                                .subcateName(projection.getSubcateName() != null ? projection.getSubcateName() : "")
                                .brandNames(projection.getBrandNames() != null ? projection.getBrandNames() : "")
                                .brandIds(Collections.emptyList())
                                .build();
        }
}
