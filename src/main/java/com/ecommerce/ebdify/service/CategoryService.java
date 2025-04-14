package com.ecommerce.ebdify.service;

import com.ecommerce.ebdify.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    void createCategory(Category category);

    Category updateCategory(Category category, Long categoryId);

    void deleteCategory(Long categoryId);
}