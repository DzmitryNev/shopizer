package com.salesmanager.core.business.services.catalog.product.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductListTO {

    private Long id = 0L;
    private BigDecimal price;
    private int quantity = 0;
    private String sku;
    private boolean productShipeable = false;
    private boolean preOrder = false;
    private boolean productVirtual = false;
    private int quantityOrderMaximum =-1;
    private int quantityOrderMinimum = 1;
    private Boolean productIsFree;
    private Boolean available;
    private Boolean visible;
    private BigDecimal productLength;
    private BigDecimal productWidth;
    private BigDecimal productHeight;
    private BigDecimal productWeight;
    private Double rating = 0D;
    private Integer ratingCount;
    private Integer sortOrder;
    private String dateAvailable;
    private String refSku;
}
