package com.salesmanager.shop.admin.controller.products.com.salesmanager.shop;

import javax.websocket.server.PathParam;
import java.util.List;

import com.salesmanager.core.business.services.catalog.product.model.ProductListTO;
import com.salesmanager.core.business.services.catalog.product.model.ProductTO;
import com.salesmanager.shop.product.service.ProductServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("restProductController")
@RequestMapping(value = "api/v1/products", produces = MediaType
        .APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    ProductServiceWrapper productServiceWrapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<ProductListTO> retrieveList() {
        return productServiceWrapper.retrieveList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductTO retrieveOne(@PathVariable("id") Long id) {
        return productServiceWrapper.retrieveOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createProduct(@PathVariable("id") Long id) {
        // not implement
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateProduct(@PathVariable("id") Long id) {
        // not implement
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public void findProduct(@PathParam("searchStr") String productSearch) {

    }
}
