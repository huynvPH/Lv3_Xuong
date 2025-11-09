package com.example.lv3.service.impl;

import com.example.lv3.dto.response.BrandResponse;
import com.example.lv3.model.Brand;
import com.example.lv3.repository.*;
import com.example.lv3.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BrandSerimpl implements BrandService {

    private final BrandRepository thuongHieuRepository;

    @Override
    public List<BrandResponse> layTatCaThuongHieu() {
        // Lấy tất cả thương hiệu từ database
        List<Brand> danhSachThuongHieu = thuongHieuRepository.findAll();
        // Chuyển đổi từ entity sang DTO response
        return danhSachThuongHieu.stream()
                .map(thuongHieu -> BrandResponse.builder()
                        .id(thuongHieu.getId())
                        .brandName(thuongHieu.getBrandName())
                        .build())
                .collect(Collectors.toList());
    }
}
