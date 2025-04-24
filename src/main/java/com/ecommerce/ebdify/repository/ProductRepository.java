package com.ecommerce.ebdify.repository;

import com.ecommerce.ebdify.models.entities.Category;
import com.ecommerce.ebdify.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable page);

    Page<Product> findByProductNameLikeIgnoreCase(String productName, Pageable page);
}
