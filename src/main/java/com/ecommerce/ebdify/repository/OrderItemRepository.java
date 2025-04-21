package com.ecommerce.ebdify.repository;

import com.ecommerce.ebdify.models.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
