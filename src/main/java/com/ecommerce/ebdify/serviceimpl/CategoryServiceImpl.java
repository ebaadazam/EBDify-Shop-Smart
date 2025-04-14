package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.exceptions.APIException;
import com.ecommerce.ebdify.exceptions.ResourceNotFoundException;
import com.ecommerce.ebdify.model.Category;
import com.ecommerce.ebdify.repository.CategoryRepository;
import com.ecommerce.ebdify.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("Categories do not exists so far. Please create one!");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already exists");
        }
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category updatedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));
        updatedCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
       Category category = categoryRepository.findById(categoryId)
               .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));
       categoryRepository.delete(category);
    }
}
