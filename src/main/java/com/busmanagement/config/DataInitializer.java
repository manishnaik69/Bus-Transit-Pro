package com.busmanagement.config;

import com.busmanagement.model.ERole;
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
        initTestUsers();
    }

    /**
     * Initialize role data
     */
    private void initRoles() {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            for (ERole roleEnum : ERole.values()) {
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
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User(
                    "admin",
                    "admin@busmanagement.com",
                    passwordEncoder.encode("admin123"),
                    "System Administrator"
            );
            adminUser.setRole(User.UserRole.ADMIN);
            adminUser.setPhoneNumber("9876543210");
            
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(ERole.ROLE_ADMIN).ifPresent(roles::add);
            adminUser.setRoles(roles);
            
            userRepository.save(adminUser);
        }
    }
    
    /**
     * Initialize test users for each role
     */
    private void initTestUsers() {
        // Passenger user
        if (!userRepository.existsByUsername("passenger")) {
            User user = new User(
                    "passenger",
                    "passenger@example.com",
                    passwordEncoder.encode("password"),
                    "Test Passenger"
            );
            user.setRole(User.UserRole.PASSENGER);
            user.setPhoneNumber("1234567890");
            
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(ERole.ROLE_PASSENGER).ifPresent(roles::add);
            user.setRoles(roles);
            
            userRepository.save(user);
        }
        
        // Driver user
        if (!userRepository.existsByUsername("driver")) {
            User user = new User(
                    "driver",
                    "driver@example.com",
                    passwordEncoder.encode("password"),
                    "Test Driver"
            );
            user.setRole(User.UserRole.DRIVER);
            user.setPhoneNumber("2345678901");
            
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(ERole.ROLE_DRIVER).ifPresent(roles::add);
            user.setRoles(roles);
            
            userRepository.save(user);
        }
        
        // Maintenance user
        if (!userRepository.existsByUsername("maintenance")) {
            User user = new User(
                    "maintenance",
                    "maintenance@example.com",
                    passwordEncoder.encode("password"),
                    "Test Maintenance Staff"
            );
            user.setRole(User.UserRole.MAINTENANCE_STAFF);
            user.setPhoneNumber("3456789012");
            
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(ERole.ROLE_MAINTENANCE).ifPresent(roles::add);
            user.setRoles(roles);
            
            userRepository.save(user);
        }
    }
}