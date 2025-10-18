package com.example.lv3.repository;

import com.example.lv3.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
	List<Brand> findAllByIdIn(List<Long> id);
	
}
