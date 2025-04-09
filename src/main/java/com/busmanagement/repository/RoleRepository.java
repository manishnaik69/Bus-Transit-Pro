package com.busmanagement.repository;

import com.busmanagement.model.ERole;
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
     * Find a role by name
     * 
     * @param name Role name to search for
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(ERole name);
}