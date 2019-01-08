package com.salesmanager.shop.product.converter;

import com.salesmanager.core.business.services.catalog.product.model.ProductListTO;
import com.salesmanager.shop.model.catalog.product.ReadableProduct;
import org.springframework.stereotype.Component;

@Component
public class ProductListConverter implements Converter<ReadableProduct, ProductListTO> {

    @Override
    public ProductListTO convert(ReadableProduct source) {
        ProductListTO target = new ProductListTO();
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
        return target;
    }
}
