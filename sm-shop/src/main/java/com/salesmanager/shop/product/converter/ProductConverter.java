package com.salesmanager.shop.product.converter;

import com.salesmanager.core.business.services.catalog.product.model.ProductDescriptionTO;
import com.salesmanager.core.business.services.catalog.product.model.ProductTO;
import com.salesmanager.shop.model.catalog.product.ReadableProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<ReadableProduct, ProductTO> {

    @Autowired
    Converter<com.salesmanager.shop.model.catalog.product.ProductDescription, ProductDescriptionTO>
            productDescriptionConverter;

    @Override
    public ProductTO convert(ReadableProduct source) {
        ProductTO target = new ProductTO();
        target.setId(source.getId());
        target.setSku(source.getRefSku());

        target.setId(source.getId());
        target.setAvailable(source.isAvailable());
        target.setProductHeight(source.getProductHeight());
        target.setProductLength(source.getProductLength());
        target.setProductWeight(source.getProductWeight());
        target.setProductWidth(source.getProductWidth());
        target.setPreOrder(source.isPreOrder());
        target.setRefSku(source.getRefSku());
        target.setSortOrder(source.getSortOrder());

        ProductDescriptionTO productDescriptionTO = productDescriptionConverter.convert(source.getDescription());
        target.setDescription(productDescriptionTO);
        return target;
    }
}
