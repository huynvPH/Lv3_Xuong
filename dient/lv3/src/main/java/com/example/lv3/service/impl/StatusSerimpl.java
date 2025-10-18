package com.example.lv3.service.impl;

import com.example.lv3.dto.response.StatusResponse;
import com.example.lv3.model.Status;
import com.example.lv3.repository.*;
import com.example.lv3.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatusSerimpl implements StatusService {
    private final StatusRepository trangThaiRepository;

    @Override
    public List<StatusResponse> layTatCaTrangThai() {
        // Lấy tất cả trạng thái từ database
        List<Status> danhSachTrangThai = trangThaiRepository.findAll();
        // Chuyển đổi từ entity sang DTO response
        return danhSachTrangThai.stream()
                .map(trangThai -> StatusResponse.builder()
                        .id(trangThai.getId())
                        .statusName(trangThai.getStatusName())
                        .build())
                .collect(Collectors.toList());
    }

}
