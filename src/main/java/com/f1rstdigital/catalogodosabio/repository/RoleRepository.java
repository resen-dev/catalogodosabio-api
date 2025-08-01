package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
