package com.example.lv3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {
        private Long id;

        private String productName;

        private String color;

        private Integer quantity;

        private BigDecimal sellPrice;

        private BigDecimal originPrice;

        private String statusName;

        private Long statusId;

        private String subcateName;

        private Long subcateId;

        private String brandNames;

        private List<Long> brandIds;


}
