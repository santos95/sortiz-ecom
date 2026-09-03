package com.ecommerce.sortizecom.service;

import com.ecommerce.sortizecom.exceptions.APIException;
import com.ecommerce.sortizecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sortizecom.model.Category;
import com.ecommerce.sortizecom.payload.CategoryDTO;
import com.ecommerce.sortizecom.payload.CategoryResponse;
import com.ecommerce.sortizecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(final CategoryRepository categoryRepository,
                               final ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if (existingCategory != null) {

            throw new APIException("Category with the name " + categoryDTO.getCategoryName() + " already exists!");
        }

        Category savedCategory = this.categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = this.modelMapper.map(savedCategory, CategoryDTO.class);

        return savedCategoryDTO;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

//        List<CategoryDTO> categories = this.categoryRepository.findAll()
//                .stream()
//                .map(c -> new CategoryDTO(
//                        c.getCategoryId(),
//                        c.getCategoryName()
//                        ))
//                .toList();

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetail = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetail);
        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()) {
            throw new APIException("No category created till now!");
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        // set categories response object
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContet(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryID) {

        Category category = this.categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        this.categoryRepository.delete(category);

        return this.modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {

        // check if the category exists - if not exists throw and exception
        Category savedCategory = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryId));

        Category updatedCategory = this.modelMapper.map(categoryDTO, Category.class);
        updatedCategory.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(updatedCategory);

        return this.modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
