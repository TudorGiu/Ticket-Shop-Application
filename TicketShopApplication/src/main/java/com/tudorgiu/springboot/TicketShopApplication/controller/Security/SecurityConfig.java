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

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password,'true' from users where email=?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, r.rolename FROM users u JOIN user_roles ur ON u.id = ur.user_id JOIN roles r ON ur.role_id = r.id WHERE u.email=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/","/css/**","/users/registerPage","/users/save","/events/").permitAll()
                        .anyRequest().authenticated()
            )
            .formLogin(form ->
                form
                        .loginPage("/users/loginPage")
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
