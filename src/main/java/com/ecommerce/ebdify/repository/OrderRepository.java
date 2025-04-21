package com.ecommerce.ebdify.repository;

import com.ecommerce.ebdify.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
