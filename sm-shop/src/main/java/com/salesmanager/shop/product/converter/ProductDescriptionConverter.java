package com.salesmanager.shop.product.converter;

import com.salesmanager.core.business.services.catalog.product.model.ProductDescriptionTO;
import com.salesmanager.shop.model.catalog.product.ProductDescription;
import org.springframework.stereotype.Component;

@Component
public class ProductDescriptionConverter implements Converter<com.salesmanager.shop.model.catalog.product.ProductDescription, ProductDescriptionTO> {

    @Override
    public ProductDescriptionTO convert(ProductDescription source) {
        ProductDescriptionTO target = new ProductDescriptionTO();
        target.setName(source.getName());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setFriendlyUrl(source.getFriendlyUrl());
        target.setHighlights(source.getHighlights());
        target.setKeyWords(source.getKeyWords());
        target.setLanguage(source.getLanguage());
        target.setMetaDescription(source.getMetaDescription());
        return target;
    }
}
