package com.plazoleta.plazoleta.common.configurations.beans;

import com.plazoleta.plazoleta.domain.ports.in.CategoryServicePort;
import com.plazoleta.plazoleta.domain.ports.in.DishServicePort;
import com.plazoleta.plazoleta.domain.ports.in.OrderServicePort;
import com.plazoleta.plazoleta.domain.ports.in.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.ports.out.*;
import com.plazoleta.plazoleta.domain.usecases.CategoryUseCase;
import com.plazoleta.plazoleta.domain.usecases.DishUseCase;
import com.plazoleta.plazoleta.domain.usecases.OrderUseCase;
import com.plazoleta.plazoleta.domain.usecases.RestaurantUseCase;
import com.plazoleta.plazoleta.infrastructure.adapters.persistence.CategoryPersistenceAdapter;
import com.plazoleta.plazoleta.infrastructure.adapters.persistence.DishPersistenceAdapter;
import com.plazoleta.plazoleta.infrastructure.adapters.persistence.OrderPersistenceAdapter;
import com.plazoleta.plazoleta.infrastructure.adapters.persistence.RestaurantPersistenceAdapter;
import com.plazoleta.plazoleta.infrastructure.mappers.*;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.CategoryRepository;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.DishRepository;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.OrderRepository;
import com.plazoleta.plazoleta.infrastructure.repositories.mysql.RestaurantRepository;
import com.plazoleta.plazoleta.infrastructure.security.JwtAuthenticationFilter;
import com.plazoleta.plazoleta.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;
    private final OrderRepository orderRepository;
    private final JwtUtil jwtUtil;
    private final OrderItemEntityMapper itemMapper;
    private final OrderEntityMapper orderEntityMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;
    private final NotificationClientPort notificationClient;
    private final TraceabilityClientPort traceabilityClientPort;


    @Bean
    public RestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort());
    }

    @Bean
    public RestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantPersistenceAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public DishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(),restaurantPersistencePort());
    }

    @Bean
    public DishPersistencePort dishPersistencePort() {
        return new DishPersistenceAdapter(dishRepository, dishEntityMapper, restaurantRepository);
    }

    @Bean public CategoryPersistencePort categoryPersistencePort(){
        return new CategoryPersistenceAdapter(categoryRepository,categoryEntityMapper);
    }

    @Bean public CategoryServicePort categoryServicePort(){
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    public OrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(),dishPersistencePort(),restaurantPersistencePort(),jwtUtil,
                notificationClient);
    }

    @Bean
    public OrderPersistencePort orderPersistencePort() {
        return new OrderPersistenceAdapter(orderRepository, restaurantRepository, dishRepository, itemMapper,
                traceabilityClientPort, orderEntityMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicApiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/restaurant/get",
                        "/api/v1/dish/{restaurantId}/dishes",
                        "/api/v1/trazabilidad/**"
                )
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
                        .requestMatchers(HttpMethod.POST, "/api/v1/restaurant/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/dish/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/order/").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/v1/order/restaurants/{restaurantId}/orders").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/order/{id}/assign").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/order/{id}/status").hasRole("EMPLOYEE")


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
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

