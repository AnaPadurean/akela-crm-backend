package com.example.akela.swim.crm.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "akl.jwt")
public record JwtProperties(
        String secret,
        long expirationSeconds,
        String issuer
) {}
