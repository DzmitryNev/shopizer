package com.salesmanager.shop.product.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.exception.ServiceRuntimeException;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.model.ProductListTO;
import com.salesmanager.core.business.services.catalog.product.model.ProductTO;
import com.salesmanager.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.relationship.ProductRelationship;
import com.salesmanager.core.model.catalog.product.relationship.ProductRelationshipType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.catalog.product.ReadableProduct;
import com.salesmanager.shop.populator.catalog.ReadableProductPopulator;
import com.salesmanager.shop.product.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductServiceWrapperImpl implements ProductServiceWrapper {

    @Autowired
    ProductService productService;
    @Autowired
    ReadableProductPopulator readableProductPopulator;

    @Autowired
    LanguageService languageService;
    @Autowired
    MerchantStoreService merchantStoreService;

    @Autowired
    Converter<ReadableProduct, ProductListTO> productListConverter;
    @Autowired
    Converter<ReadableProduct, ProductTO> productConverter;

    @Autowired
    ProductRelationshipService productRelationshipService;

    @Transactional(readOnly = true)
    @Override
    public List<ProductListTO> retrieveList() {
        try {
            Language language = languageService.getByCode("en");
            MerchantStore store = merchantStoreService.getByCode("DEFAULT");
            List<ProductRelationship> relationships = productRelationshipService.getByType(store,
                    ProductRelationshipType
                            .FEATURED_ITEM, language);

            return relationships.stream()
                    .map(ProductRelationship::getRelatedProduct)
                    .map(product -> readableProductPopulator.populate(product, new ReadableProduct(), store, language))
                    .map(productListConverter::convert)
                    .collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ServiceRuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ProductTO retrieveOne(Long id) {
        Objects.requireNonNull(id, "id must be not null");

        try {
            Product product = productService.getById(id);
            MerchantStore store = merchantStoreService.getByCode("DEFAULT");
            Language language = languageService.getByCode("en");

            ReadableProduct productProxy = readableProductPopulator.populate(product, new ReadableProduct(), store,
                    language);

            return productConverter.convert(productProxy);
        } catch (ServiceException e) {
            throw new ServiceRuntimeException(e);
        }
    }

    @Override
    public ProductTO createProduct(Long id) {
        return null;
    }

    @Override
    public ProductTO updateProduct(Long id) {
        return null;
    }

    @Override
    public List<ProductTO> findProduct(String productSearch) {

        //        productService.findProductByName();
        return null;
    }
}
