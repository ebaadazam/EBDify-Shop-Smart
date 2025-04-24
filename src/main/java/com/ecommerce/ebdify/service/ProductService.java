package com.ecommerce.ebdify.service;

import com.ecommerce.ebdify.models.dtos.request.ProductDTO;
import com.ecommerce.ebdify.models.dtos.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder, String keyword, String category);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder);

    ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortByOrder);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException;
}
