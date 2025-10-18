package com.example.lv3.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sub_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sub_cate_code", length = 20)
	private String subCateCode;

	@Column(name = "sub_cate_name", length = 50)
	private String subCateName;

	@ManyToOne
	@JoinColumn(name = "cate_id")
	private Category category;

	@OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL)
	private List<Product> products;
}
