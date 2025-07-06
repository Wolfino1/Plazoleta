package com.plazoleta.plazoleta.common.configurations.beans;

import com.plazoleta.plazoleta.domain.ports.in.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.usecases.RestaurantUseCase;
import com.plazoleta.plazoleta.infrastructure.adapters.persistence.RestaurantPersistenceAdapter;
import com.plazoleta.plazoleta.infrastructure.mappers.RestaurantEntityMapper;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.RestaurantRepository;
import com.plazoleta.plazoleta.infrastructure.security.JwtAuthenticationFilter;
import com.plazoleta.plazoleta.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class BeanConfiguration {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final JwtUtil jwtUtil;


    @Bean
    public RestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort());
    }

    @Bean
    public RestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantPersistenceAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicApiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/restaurant/")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain protectedApiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/**")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

