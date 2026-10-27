package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;
import com.ecommerce.sortizecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    ProductResponse getAllProducts();
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse searchProductsByCategory(Long categoryId);
    ProductResponse searchProductsByCategory(long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse searchProductsByKeyworkd(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
