package com.salesmanager.core.business.services.catalog.product.model;

import lombok.Data;

@Data
public class ProductDescriptionTO {
    private String name;
    private String description;
    private String friendlyUrl;
    private String keyWords;
    private String highlights;
    private String metaDescription;
    private String title;
    private String language;

}
