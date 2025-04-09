package com.busmanagement.config;

import com.busmanagement.model.Role;
import com.busmanagement.model.User;
import com.busmanagement.repository.RoleRepository;
import com.busmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Component to initialize data for the application
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Initialize data on application startup
     */
    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initAdminUser();
    }

    /**
     * Initialize role data
     */
    private void initRoles() {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            for (Role.ERole roleEnum : Role.ERole.values()) {
                Role role = new Role();
                role.setName(roleEnum);
                roleRepository.save(role);
            }
        }
    }

    /**
     * Initialize admin user
     */
    private void initAdminUser() {
        // Create admin user if no users exist
        if (userRepository.count() == 0) {
            User adminUser = new User(
                    "admin",
                    "admin@busmanagement.com",
                    passwordEncoder.encode("admin123"),
                    "System Administrator"
            );
            adminUser.setRole(User.UserRole.ADMIN);
            adminUser.setPhoneNumber("9876543210");
            
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(Role.ERole.ROLE_ADMIN).ifPresent(roles::add);
            adminUser.setRoles(roles);
            
            userRepository.save(adminUser);
        }
    }
}