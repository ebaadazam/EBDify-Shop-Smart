package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.model.Category;
import com.ecommerce.ebdify.repository.CategoryRepository;
import com.ecommerce.ebdify.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category updatedCategory = categoryRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        updatedCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
       Category category = categoryRepository.findById(id)
               .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
       categoryRepository.delete(category);
    }
}
