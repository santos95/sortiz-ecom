package com.ecommerce.sortizecom.controller;

import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;
import com.ecommerce.sortizecom.payload.ProductResponse;
import com.ecommerce.sortizecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody Product product,
            @PathVariable Long categoryId) {

        ProductDTO productDTO = this.productService.addProduct(product, categoryId);

        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(){

        ProductResponse productResponse = this.productService.getAllProducts();

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categorgyId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categorgyId){

        ProductResponse productResponse = this.productService.searchProductsByCategory(categorgyId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
