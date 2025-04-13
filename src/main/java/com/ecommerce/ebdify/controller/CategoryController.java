package com.ecommerce.ebdify.controller;

import com.ecommerce.ebdify.model.Category;
import com.ecommerce.ebdify.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        String created = "Category with " + category.getCategoryName() + " added successfully";
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody Category category,
                                 @PathVariable Long id) {
           try {
               Category updatedCategory = categoryService.updateCategory(category, id);
               return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
           } catch (ResponseStatusException e) {
               return new ResponseEntity<>(e.getReason(), e.getStatusCode());
           }
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            String message = "Category with Id " + id + " deleted successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
