package com.example.lv3.repository;

import com.example.lv3.dto.response.ProductResponse;
import com.example.lv3.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
//generic
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query(value = """
        SELECT
            p.id AS id,
            p.product_name AS productName,
            p.color AS color,
            p.quantity AS quantity,
            p.sell_price AS sellPrice,
            p.origin_price AS originalPrice, -- Sửa từ original_price thành originalPrice
            s.status_name AS statusName,
            sc.sub_cate_name AS subCateName,
            STRING_AGG(b.brand_name, ',') AS brandNames
        FROM product p
        LEFT JOIN status s ON p.status_id = s.id
        LEFT JOIN sub_category sc ON p.subcate_id = sc.id
        LEFT JOIN category c ON sc.cate_id = c.id
        LEFT JOIN product_brand pb ON p.id = pb.product_id
        LEFT JOIN brand b ON pb.brand_id = b.id
        WHERE (:productName IS NULL OR :productName = '' OR p.product_name LIKE CONCAT('%', :productName, '%'))
          AND (:brandId IS NULL OR b.id = :brandId)
          AND (:categoryId IS NULL OR c.id = :categoryId)
          AND (:statusId IS NULL OR s.id = :statusId)
          AND (:price IS NULL OR p.sell_price = :price)
        GROUP BY
            p.id, p.product_name, p.color, p.quantity,
            p.sell_price, p.origin_price, -- Sửa từ original_price thành originalPrice
            s.status_name, sc.sub_cate_name
        """, countQuery = """
        SELECT COUNT(DISTINCT p.id) FROM product p
        LEFT JOIN status s ON p.status_id = s.id
        LEFT JOIN sub_category sc ON p.subcate_id = sc.id
        LEFT JOIN category c ON sc.cate_id = c.id
        LEFT JOIN product_brand pb ON p.id = pb.product_id
        LEFT JOIN brand b ON pb.brand_id = b.id
        WHERE (:productName IS NULL OR p.product_name LIKE CONCAT('%', :productName, '%'))
          AND (:brandId IS NULL OR b.id = :brandId)
          AND (:categoryId IS NULL OR c.id = :categoryId)
          AND (:statusId IS NULL OR s.id = :statusId)
          AND (:price IS NULL OR p.sell_price = :price)
        """, nativeQuery = true)
	Page<ProductResponse> searchProducts(
			@Param("productName") String productName,
			@Param("brandId") Long brandId,
			@Param("categoryId") Long categoryId,
			@Param("statusId") Long statusId,
			@Param("price") Float price,
			Pageable pageable
	);
}
