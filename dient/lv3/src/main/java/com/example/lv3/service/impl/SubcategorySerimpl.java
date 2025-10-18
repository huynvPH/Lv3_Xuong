package com.example.lv3.service.impl;

import com.example.lv3.dto.response.SubcategoryResponse;
import com.example.lv3.model.SubCategory;
import com.example.lv3.repository.*;
import com.example.lv3.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubcategorySerimpl implements SubcategoryService {
    private final SubCategoryRepository danhMucConRepository;


    @Override
    public List<SubcategoryResponse> layTatCaDanhMucCon() {
        // Lấy tất cả danh mục con từ database
        List<SubCategory> danhSachDanhMucCon = danhMucConRepository.findAll();
        // Chuyển đổi từ entity sang DTO response
        return danhSachDanhMucCon.stream()
                .map(danhMucCon -> SubcategoryResponse.builder()
                        .id(danhMucCon.getId())
                        .subCateName(danhMucCon.getSubCateName())
                        .build())
                .collect(Collectors.toList());
    }
}
