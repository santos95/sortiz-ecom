package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;
import com.ecommerce.sortizecom.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Product product, Long categoryId);
    ProductResponse getAllProducts();
    ProductResponse searchProductsByCategory(Long categoryId);
    ProductResponse searchProductsByKeyworkd(String keyword);
    ProductDTO updateProduct(Long productId,Product product);
}
