package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Category;
import com.ecommerce.sortizecom.payload.CategoryDTO;
import com.ecommerce.sortizecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryID);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
