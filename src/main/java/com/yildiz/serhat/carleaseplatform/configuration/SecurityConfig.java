package com.yildiz.serhat.carleaseplatform.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("serhat")
                .password("admin")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic()
                .and()
                .authorizeRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/customers/**").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/v1/customers").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/v1/customers/{id}/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/v1/customers/{id}/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/v1/cars/**").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/v1/cars").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/v1/cars/{id}/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/v1/cars/{id}/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin();

        return http.build();
    }


}