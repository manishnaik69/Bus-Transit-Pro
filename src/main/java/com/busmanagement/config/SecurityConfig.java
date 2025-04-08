package com.busmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for the Bus Management System.
 * Configures security rules, authentication, and user roles.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/index", "/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/passenger/**").hasRole("PASSENGER")
                .antMatchers("/driver/**").hasRole("DRIVER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/maintenance/**").hasRole("MAINTENANCE")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/auth/login")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/auth/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
