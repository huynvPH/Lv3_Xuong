package com.example.lv3.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.oauth2.success-redirect-uri:http://localhost:5173}")
    private String successRedirectUri;

    @Value("${app.oauth2.failure-redirect-uri:http://localhost:5173/login?error}")
    private String failureRedirectUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/error", "/login", "/login/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl(successRedirectUri, true)
                        .failureUrl(failureRedirectUri)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl(successRedirectUri)
                        .permitAll()
                );

        return http.build();
    }
}
