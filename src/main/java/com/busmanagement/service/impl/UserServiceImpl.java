package com.busmanagement.service.impl;

import com.busmanagement.dto.RegistrationDTO;
import com.busmanagement.factory.UserFactory;
import com.busmanagement.model.Role;
import com.busmanagement.model.User;
import com.busmanagement.repository.RoleRepository;
import com.busmanagement.repository.UserRepository;
import com.busmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * Provides business logic for user operations.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserFactory userFactory;

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersByRole(Long roleId) {
        return userRepository.findByRoleId(roleId);
    }

    @Override
    public List<User> findUsersByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Override
    public List<User> findActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    @Override
    @Transactional
    public User registerNewUser(RegistrationDTO registrationDTO) {
        // Validate input
        if (registrationDTO == null || registrationDTO.getUsername() == null || 
            registrationDTO.getPassword() == null || registrationDTO.getEmail() == null ||
            registrationDTO.getFullName() == null || registrationDTO.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Invalid registration details");
        }
        
        // Check if username is already taken
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Check if email is already registered
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        
        // Get or create the PASSENGER role
        Optional<Role> roleOpt = roleRepository.findByName("ROLE_PASSENGER");
        Role passengerRole;
        if (roleOpt.isPresent()) {
            passengerRole = roleOpt.get();
        } else {
            passengerRole = new Role();
            passengerRole.setName("ROLE_PASSENGER");
            passengerRole.setDescription("Regular passenger role");
            passengerRole = roleRepository.save(passengerRole);
        }
        
        // Create user using the factory pattern
        User user = userFactory.createPassenger(
            registrationDTO.getUsername(),
            registrationDTO.getPassword(),
            registrationDTO.getEmail(),
            registrationDTO.getFullName(),
            registrationDTO.getPhoneNumber(),
            registrationDTO.getAddress(),
            passengerRole
        );
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        // Validate user
        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getEmail() == null ||
            user.getFullName() == null || user.getRole() == null) {
            throw new IllegalArgumentException("Invalid user details");
        }
        
        // If it's a new user, check username and email uniqueness
        if (user.getId() == null) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username is already taken");
            }
            
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email is already registered");
            }
            
            // Encode password if it's a new user or password is changed
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        // Check if user exists
        User existingUser = findUserById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        
        // Validate user
        if (user == null || user.getUsername() == null || user.getEmail() == null ||
            user.getFullName() == null || user.getRole() == null) {
            throw new IllegalArgumentException("Invalid user details");
        }
        
        // Check username uniqueness if changed
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Check email uniqueness if changed
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        
        // Update user fields
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFullName(user.getFullName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole());
        existingUser.setActive(user.isActive());
        
        // Don't update password here, use changePassword method for that
        
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public User activateUser(Long userId) {
        User user = findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User deactivateUser(Long userId) {
        User user = findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        user.setActive(false);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        // For a real system, consider checking if the user has associated data
        // before deleting or implement soft delete
        
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public User changePassword(Long userId, String currentPassword, String newPassword) {
        User user = findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        return role.orElse(null);
    }

    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public long countUsersByRole(Long roleId) {
        return userRepository.countByRoleId(roleId);
    }
}
