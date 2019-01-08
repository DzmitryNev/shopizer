package com.salesmanager.shop.product.service;

import java.util.List;

import com.salesmanager.core.business.services.catalog.product.model.ProductListTO;
import com.salesmanager.core.business.services.catalog.product.model.ProductTO;

public interface ProductServiceWrapper {

    List<ProductListTO> retrieveList();

    ProductTO retrieveOne(Long id);

    ProductTO createProduct(Long id);

    ProductTO updateProduct(Long id);

    List<ProductTO> findProduct(String productSearch);
}
