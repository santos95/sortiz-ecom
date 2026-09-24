package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;

public interface ProductService {

    public ProductDTO addProduct(Product product, Long categoryId);
}
