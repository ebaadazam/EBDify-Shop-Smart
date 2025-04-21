package com.ecommerce.ebdify.repository;

import com.ecommerce.ebdify.models.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
