package com.example.lv3.controller;

import com.example.lv3.dto.request.ProductRequest;
import com.example.lv3.dto.response.*;
import com.example.lv3.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ProductController {
	private final BrandService thuongHieuService;
	private final CategoryService danhMucService;
	private final ProductService productService;
	private final StatusService trangThaiService;
	private final SubcategoryService danhMucConService;

	// Tìm kiếm sản phẩm với phân trang và lọc
	@GetMapping("/products")
	public ResponseEntity<ProductPageResponse> searchProducts(
			@RequestParam(required = false) String productName,
			@RequestParam(required = false, name = "price") Float price,
			@RequestParam(required = false) Long brandId,
			@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Long statusId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			Pageable pageable
	) {
		if (productName == null) productName = "";
		if (price == null) price = null;
		if (brandId == null) brandId = null;
		if (categoryId == null) categoryId = null;
		if (statusId == null) statusId = null;
		Pageable updatePageable = PageRequest.of(page, size);

		ProductPageResponse productPageResponse = productService.searchProducts(productName, brandId, categoryId, statusId, price, updatePageable);
		return ResponseEntity.ok(productPageResponse);
	}

	// Lấy thông tin chi tiết sản phẩm theo ID
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
		ProductResponse productResponse = productService.getProduct(id);
		return ResponseEntity.ok(productResponse);
	}

	// Tạo mới sản phẩm
	@PostMapping("/products")
	public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
		productService.createProduct(productRequest);
		return ResponseEntity.ok().build();
	}

	// Cập nhật sản phẩm
	@PutMapping("/products/{id}")
	public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
		productService.updateProduct(id, productRequest);
		return ResponseEntity.ok().build();
	}

	// Xóa sản phẩm
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok().build();
	}

	// Trả về trang index cho jQuery AJAX
	@GetMapping("/")
	public String index() {
		return "index";
	}

	// Lấy danh sách tất cả thương hiệu để hiển thị trong dropdown
	@GetMapping("/brands")
	public ResponseEntity<List<BrandResponse>> layTatCaThuongHieu() {
		List<BrandResponse> danhSachThuongHieu = thuongHieuService.layTatCaThuongHieu();
		return ResponseEntity.ok(danhSachThuongHieu);
	}

	// Lấy danh sách tất cả danh mục để hiển thị trong dropdown
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryResponse>> layTatCaDanhMuc() {
		List<CategoryResponse> danhSachDanhMuc = danhMucService.layTatCaDanhMuc();
		return ResponseEntity.ok(danhSachDanhMuc);
	}

	// Lấy danh sách tất cả danh mục con để hiển thị trong dropdown
	@GetMapping("/subcategories")
	public ResponseEntity<List<SubcategoryResponse>> layTatCaDanhMucCon() {
		List<SubcategoryResponse> danhSachDanhMucCon = danhMucConService.layTatCaDanhMucCon();
		return ResponseEntity.ok(danhSachDanhMucCon);
	}

	// Lấy danh sách tất cả trạng thái để hiển thị trong dropdown
	@GetMapping("/statuses")
	public ResponseEntity<List<StatusResponse>> layTatCaTrangThai() {
		List<StatusResponse> danhSachTrangThai = trangThaiService.layTatCaTrangThai();
		return ResponseEntity.ok(danhSachTrangThai);
	}
}