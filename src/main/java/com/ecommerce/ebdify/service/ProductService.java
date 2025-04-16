package com.ecommerce.ebdify.service;

import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.entities.Product;

public interface ProductService {
    ProductDTO addProduct(Product product, Long catregoryId);
}
