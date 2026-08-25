package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryID);

    Category updateCategory(Long categoryId, Category category);
}
