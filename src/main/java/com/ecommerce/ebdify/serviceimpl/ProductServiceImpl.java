package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.exceptions.ResourceNotFoundException;
import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.entities.Category;
import com.ecommerce.ebdify.models.entities.Product;
import com.ecommerce.ebdify.repository.CategoryRepository;
import com.ecommerce.ebdify.repository.ProductRepository;
import com.ecommerce.ebdify.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100));
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }
}
