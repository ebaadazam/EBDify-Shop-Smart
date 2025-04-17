package com.ecommerce.ebdify.service;

import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.dtos.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long catregoryId);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductsByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException;
}
