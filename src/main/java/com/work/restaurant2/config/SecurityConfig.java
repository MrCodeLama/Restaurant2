package com.work.restaurant2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Налаштування CORS (див. нижче)
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Дозволяємо доступ до публічних ресурсів (наприклад, меню)
                        .requestMatchers("/menu").permitAll()
                        // Інші ендпоінти захищені (наприклад, створення замовлення, отримання деталей, таблиці)
                        .requestMatchers("/tables/**", "/table-order-details/**", "/create-order/**", "/confirm-payment").authenticated()
                        .anyRequest().permitAll()
                )
                // Налаштування OAuth2 Resource Server, що використовує JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}