package com.example.lv3.repository.projection;

import java.math.BigDecimal;

public interface ProductSearchProjection {
        Long getId();

        String getProductName();

        String getColor();

        Integer getQuantity();

        BigDecimal getSellPrice();

        BigDecimal getOriginPrice();

        String getStatusName();

        String getSubcateName();

        String getBrandNames();
}
