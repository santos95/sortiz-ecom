package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    public List<Category> categories = new ArrayList<>();

    @Override
    public void createCategory(Category category) {

        this.categories.add(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categories;
    }

    @Override
    public String deleteCategory(Long categoryID) {

        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryID))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found!"));

        this.categories.remove(category);
        return "Category with categoryId: " + categoryID + " deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {

        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();

        if (optionalCategory.isPresent()) {

            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found!");
        }

    }
}
