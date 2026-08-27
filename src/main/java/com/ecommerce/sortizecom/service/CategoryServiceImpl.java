package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Category;
import com.ecommerce.sortizecom.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

//    public List<Category> categories = new ArrayList<>();
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(Category category) {

        this.categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {

        return this.categoryRepository.findAll();
    }

    @Override
    public String deleteCategory(Long categoryID) {

        Category category = this.categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found!"));

        this.categoryRepository.delete(category);

        return "Category with categoryId: " + categoryID + " deleted successfully!";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {

        // check if the category exists - if not exists throw and exception
        Category savedCategory = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found!"));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);

        return savedCategory;
    }

}
