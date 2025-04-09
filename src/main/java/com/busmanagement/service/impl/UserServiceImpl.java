package com.busmanagement.service.impl;

import com.busmanagement.dto.RegistrationDTO;
import com.busmanagement.model.ERole;
import com.busmanagement.model.Role;
import com.busmanagement.model.User;
import com.busmanagement.repository.RoleRepository;
import com.busmanagement.repository.UserRepository;
import com.busmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the UserService interface
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public List<User> findUsersByRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return List.of();
        }
        return userRepository.findByRolesContaining(role);
    }
    
    @Override
    public List<User> findUsersByRoleName(String roleName) {
        Role role = roleRepository.findByName(ERole.valueOf(roleName)).orElse(null);
        if (role == null) {
            return List.of();
        }
        return userRepository.findByRolesContaining(role);
    }
    
    @Override
    public List<User> findActiveUsers() {
        return userRepository.findByActive(true);
    }
    
    @Override
    @Transactional
    public User registerNewUser(RegistrationDTO registrationDTO) {
        if (isUsernameTaken(registrationDTO.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        if (isEmailTaken(registrationDTO.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setActive(true);
        
        Set<Role> roles = new HashSet<>();
        // By default, assign ROLE_PASSENGER
        Role userRole = roleRepository.findByName(ERole.ROLE_PASSENGER)
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_PASSENGER"));
        roles.add(userRole);
        
        // If role specified, add it
        if (registrationDTO.getRole() != null && !registrationDTO.getRole().isEmpty()) {
            try {
                ERole roleEnum = ERole.valueOf(registrationDTO.getRole());
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleEnum));
                roles.add(role);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role name: " + registrationDTO.getRole());
            }
        }
        
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User registerNewUser(User user) {
        if (isUsernameTaken(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        if (isEmailTaken(user.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }
        
        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Ensure user is active
        user.setActive(true);
        
        // If no roles assigned, set default role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_PASSENGER)
                    .orElseThrow(() -> new RuntimeException("Role not found: ROLE_PASSENGER"));
            roles.add(userRole);
            user.setRoles(roles);
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check if username is being changed and is not taken
        if (!existingUser.getUsername().equals(updatedUser.getUsername()) && 
                isUsernameTaken(updatedUser.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        // Check if email is being changed and is not taken
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && 
                isEmailTaken(updatedUser.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }
        
        // Update user details
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhone(updatedUser.getPhone());
        
        // Only update password if provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        
        // Update roles if provided
        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            existingUser.setRoles(updatedUser.getRoles());
        }
        
        return userRepository.save(existingUser);
    }
    
    @Override
    @Transactional
    public User activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setActive(true);
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setActive(false);
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    
    @Override
    @Transactional
    public User changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Update to new password
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
        try {
            ERole roleEnum = ERole.valueOf(name);
            return roleRepository.findByName(roleEnum).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public long countAllUsers() {
        return userRepository.count();
    }
    
    @Override
    public long countUsersByRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return 0;
        }
        return userRepository.countByRolesContaining(role);
    }
}