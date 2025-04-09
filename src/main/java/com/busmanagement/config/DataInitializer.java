package com.busmanagement.config;

import com.busmanagement.model.ERole;
import com.busmanagement.model.Role;
import com.busmanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * Component to initialize database with essential data when the application starts
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initRoles();
    }

    /**
     * Initialize roles in the database
     */
    private void initRoles() {
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                new Role(ERole.ROLE_PASSENGER),
                new Role(ERole.ROLE_DRIVER),
                new Role(ERole.ROLE_ADMIN),
                new Role(ERole.ROLE_MAINTENANCE)
            );
            
            roleRepository.saveAll(roles);
            System.out.println("Initialized user roles in database");
        }
    }
}