package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;
import com.ecommerce.sortizecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    ProductResponse getAllProducts();
    ProductResponse searchProductsByCategory(Long categoryId);
    ProductResponse searchProductsByKeyworkd(String keyword);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
