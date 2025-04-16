package com.ecommerce.ebdify.repository;

import com.ecommerce.ebdify.models.dtos.response.ProductResponse;
import com.ecommerce.ebdify.models.entities.Category;
import com.ecommerce.ebdify.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String productName);
}
