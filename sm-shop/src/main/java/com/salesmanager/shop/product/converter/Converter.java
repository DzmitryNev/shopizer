package com.salesmanager.shop.product.converter;

import com.salesmanager.shop.model.catalog.product.ProductDescription;

public interface Converter<T1, T2> {

    T2 convert(T1 t);
}
