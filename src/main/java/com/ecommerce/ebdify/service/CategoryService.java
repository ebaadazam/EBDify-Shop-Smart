package com.ecommerce.ebdify.service;

import com.ecommerce.ebdify.models.dtos.request.CategoryDTO;
import com.ecommerce.ebdify.models.dtos.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

    CategoryDTO deleteCategory(Long categoryId);
}