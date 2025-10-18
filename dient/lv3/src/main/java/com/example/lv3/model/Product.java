package com.example.lv3.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "subcate_id")
	private SubCategory subCategory;

	@Column(name = "product_name", length = 100)
	private String productName;

	@Column(name = "color", length = 50)
	private String color;

	private Integer quantity;

	@Column(name = "sell_price")
	private BigDecimal sellPrice;

	@Column(name = "origin_price")
	private BigDecimal originalPrice;

	@Column(length = 1000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToMany
	@JoinTable(
			name = "product_brand",
//			schema = "dbo",                // thêm dòng này
			joinColumns = @JoinColumn(name = "product_id"),
			inverseJoinColumns = @JoinColumn(name = "brand_id")
	)
	private List<Brand> brands;



}
