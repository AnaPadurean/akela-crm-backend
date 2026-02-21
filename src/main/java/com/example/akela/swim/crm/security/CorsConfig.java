package com.example.akela.swim.crm.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // split după virgule
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        cfg.setAllowedOrigins(origins);

        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Important: pentru Bearer JWT ai nevoie de Authorization
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Nu e obligatoriu să expui Authorization (browserul oricum trimite header-ul la request).
        // Expune doar dacă frontend-ul chiar citește ceva din response headers.
        cfg.setExposedHeaders(List.of("Authorization"));

        // Pentru JWT în Authorization header, NU ai nevoie de credentials (cookies).
        // Dacă pui true, browserul devine mai strict cu origins (nu merge cu '*').
        cfg.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}

