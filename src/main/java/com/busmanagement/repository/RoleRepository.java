package com.busmanagement.repository;

import com.busmanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Role entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Find a role by its name
     *
     * @param name the role name
     * @return the optional role
     */
    Optional<Role> findByName(Role.ERole name);
}