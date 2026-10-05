package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.exceptions.APIException;
import com.ecommerce.sortizecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sortizecom.model.Category;
import com.ecommerce.sortizecom.model.Product;
import com.ecommerce.sortizecom.payload.ProductDTO;
import com.ecommerce.sortizecom.payload.ProductResponse;
import com.ecommerce.sortizecom.repositories.CategoryRepository;
import com.ecommerce.sortizecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    // get value from properties
    @Value("${project.images}")
    private String imagesPath;

    @Autowired
    public ProductServiceImpl(
            final ProductRepository productRepository,
            final CategoryRepository categoryRepository,
            final ModelMapper modelMapper,
            final FileService fileService) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {

        // get category of the product
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        // get list of products from category
        List<Product> products = category.getProducts();

        // loop over the products list and check if exists
        for (Product product : products) {
            // if products is in the list, change the flag to false and breaks the loop
            if (product.getProductName().equals(productDTO.getProductName())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {

            // convert the dto into a product entity class
            Product product = this.modelMapper.map(productDTO, Product.class);

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

        } else {

            throw new APIException("Product already exists!");
        }
    }

    @Override
    public ProductResponse getAllProducts() {

        List<Product> products = this.productRepository.findAll();

        // check if the list has zero products
        if (products.isEmpty()) {

            throw new APIException("No product created till now!");
        }

        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> this.modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);

        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByCategory(Long categoryId) {

        // get the category
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        // get all products by the category
        List<Product> products = this.productRepository.findByCategoryOrderByPriceAsc(category);

        if (products.isEmpty()) {

            throw new APIException("No product created for the category till now!");
        }

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> this.modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);

        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyworkd(String keyword) {

        List<Product> products = this.productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");

        if (products.isEmpty()) {

            throw new APIException("No product was found with the keyword!");
        }

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> this.modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse response = new ProductResponse();
        response.setContent(productDTOs);

        return response;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        // get product from db
        Product savedProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = this.modelMapper.map(productDTO, Product.class);

        // update product informacion with the one in the request body
        savedProduct.setProductName(product.getProductName());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setQuantity(product.getQuantity());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setDiscount(product.getDiscount());
        // set the special price - after discount
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01 * product.getPrice());
        savedProduct.setSpecialPrice(specialPrice);


        savedProduct = this.productRepository.save(savedProduct);

        return this.modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        this.productRepository.delete(product);

        return this.modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        // get product from the database
        Product savedProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // upload the image (locally or server like s3 service)
        // get filename of uploaded image
        String filename =  this.fileService.uploadImage(imagesPath, image);

        // update filename from to the product
        savedProduct.setImage(filename);

        // save product
        Product updatedProduct = this.productRepository.save(savedProduct);

        // return dto
        return this.modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
