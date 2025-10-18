package com.example.lv3.service.impl;

import com.example.lv3.dto.response.CategoryResponse;
import com.example.lv3.model.Category;
import com.example.lv3.repository.*;
import com.example.lv3.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategorySerimpl implements CategoryService {

    private final CategoryRepository danhMucRepository;



    @Override
    public List<CategoryResponse> layTatCaDanhMuc() {
        // Lấy tất cả danh mục từ database
        List<Category> danhSachDanhMuc = danhMucRepository.findAll();
        // Chuyển đổi từ entity sang DTO response
        return danhSachDanhMuc.stream()
                .map(danhMuc -> CategoryResponse.builder()
                        .id(danhMuc.getId())
                        .cateName(danhMuc.getCateName())
                        .build())
                .collect(Collectors.toList());
    }
}
