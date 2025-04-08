package com.busmanagement.config;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of UserDetailsService to break circular dependency
 */
@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This is a temporary implementation for development
        // In production, this would load users from the database
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        } else if ("passenger".equals(username)) {
            return User.builder()
                    .username("passenger")
                    .password(new BCryptPasswordEncoder().encode("passenger"))
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_PASSENGER")))
                    .build();
        } else if ("driver".equals(username)) {
            return User.builder()
                    .username("driver")
                    .password(new BCryptPasswordEncoder().encode("driver"))
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_DRIVER")))
                    .build();
        } else if ("maintenance".equals(username)) {
            return User.builder()
                    .username("maintenance")
                    .password(new BCryptPasswordEncoder().encode("maintenance"))
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_MAINTENANCE")))
                    .build();
        }
        
        throw new UsernameNotFoundException("User not found: " + username);
    }
}