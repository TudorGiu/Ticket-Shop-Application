package com.tudorgiu.springboot.TicketShopApplication.controller.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    // Bean for providing password encoding functionality
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean for managing user details using JDBC
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Set SQL queries for retrieving user details and authorities
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password,'true' from users where email=?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, r.rolename FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN roles r ON ur.role_id = r.id WHERE u.email=?");

        return jdbcUserDetailsManager;
    }

    // Bean for defining security filter chain configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // Configure security rules
        http.authorizeHttpRequests(configurer ->
                configurer
                        // Permit access to specified paths without authentication
                        .requestMatchers("/","/css/**","/users/registerPage","/users/save","/events/").permitAll()
                        // Require authentication for any other request
                        .anyRequest().authenticated()
            )
            // Configure form-based login
            .formLogin(form ->
                form
                        .loginPage("/users/loginPage")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("email")
                        .permitAll()
            )
            // Configure logout
            .logout(logout ->
                logout
                        // Permit all users to access the logout URL
                        .permitAll()
                        .logoutSuccessUrl("/")
            );

        // Build and return the configured security filter chain
        return http.build();
    }

}
