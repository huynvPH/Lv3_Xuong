package com.example.lv3.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "status_name", length = 100)
	private String statusName;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
	private List<Product> products;
}
