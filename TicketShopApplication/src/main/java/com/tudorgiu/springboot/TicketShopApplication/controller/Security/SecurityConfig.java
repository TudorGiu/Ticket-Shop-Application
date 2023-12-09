package com.tudorgiu.springboot.TicketShopApplication.controller.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails tudor = User.builder()
                .username("tudorgiu@gmail.com")
                .password("{noop}tudor")
                .roles("CUSTOMER", "ADMINISTRATOR")
                .build();

        return new InMemoryUserDetailsManager(tudor);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
            )
            .formLogin(form ->
                form
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("email")
                        .permitAll()
            )
            .logout(logout ->
                logout
                        .permitAll()
                        .logoutSuccessUrl("/")
            );
        return http.build();
    }
}
