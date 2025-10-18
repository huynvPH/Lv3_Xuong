package com.example.lv3.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cate_code", length = 20)
	private String cateCode;

	@Column(name = "cate_name", length = 50)
	private String cateName;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<SubCategory> subCategories;
}
