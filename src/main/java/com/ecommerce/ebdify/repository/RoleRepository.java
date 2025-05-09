package com.ecommerce.ebdify.repository;

import com.ecommerce.ebdify.constants.AppRole;
import com.ecommerce.ebdify.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
