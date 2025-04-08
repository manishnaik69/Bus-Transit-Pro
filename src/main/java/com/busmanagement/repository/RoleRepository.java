package com.busmanagement.repository;

import com.busmanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entities.
 * Provides methods to interact with the roles table in the database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Finds a role by its name.
     * 
     * @param name Name of the role
     * @return Optional containing the role, if found
     */
    Optional<Role> findByName(String name);
    
    /**
     * Checks if a role with the given name exists.
     * 
     * @param name Name of the role
     * @return true if the role exists, false otherwise
     */
    boolean existsByName(String name);
}
