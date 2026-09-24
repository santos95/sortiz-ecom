package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sortizecom.model.Category;
import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;
import com.ecommerce.sortizecom.payload.ProductResponse;
import com.ecommerce.sortizecom.repositories.CategoryRepository;
import com.ecommerce.sortizecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(
            final ProductRepository productRepository,
            final CategoryRepository categoryRepository,
            final ModelMapper modelMapper) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {

        // get category of the product
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        product.setCategory(category);

        // set the special price - after discount
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01 * product.getPrice());
        product.setSpecialPrice(specialPrice);

        // save the image
        product.setImage("default.png");

        // save product
        Product savedProduct = this.productRepository.save(product);

        // conver to dto and return
        return this.modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {

        List<Product> products = this.productRepository.findAll();

        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> this.modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);

        return productResponse;
    }
}
