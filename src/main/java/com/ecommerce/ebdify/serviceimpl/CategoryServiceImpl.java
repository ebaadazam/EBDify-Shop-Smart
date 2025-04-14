package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.exceptions.APIException;
import com.ecommerce.ebdify.exceptions.ResourceNotFoundException;
import com.ecommerce.ebdify.models.dtos.request.CategoryDTO;
import com.ecommerce.ebdify.models.dtos.response.CategoryResponse;
import com.ecommerce.ebdify.models.entities.Category;
import com.ecommerce.ebdify.repository.CategoryRepository;
import com.ecommerce.ebdify.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Method to fetch all the categories along with pagination and sorting
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,
            String sortBy, String sortByOrder) {
        // For sorting
        Sort sortByAndOrder = sortByOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // For Pagination
        // Pageable is an interface representing the request for specific page with
        // data from database query result
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // This will find all the results with respect to entered pageNumber & pageSize
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        // Getting the paginated content and storing it in a list of type Category
        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()) {
            throw new APIException("Categories do not exists so far. Please create one!");
        }

        // Conversion
        List<CategoryDTO> categoryDTOS = categories
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    // Method to create a category
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        // If a category with the same name exists
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    // Method to update a category through categoryId & request body
    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        // Check if the category to be updated exists before updating
        Category categoryExist = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        categoryExist = categoryRepository.save(category);
        return modelMapper.map(categoryExist, CategoryDTO.class);
    }

    // Method to delete a category through categoryId
    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
       Category category = categoryRepository.findById(categoryId)
               .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }
}
