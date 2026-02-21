package com.example.akela.swim.crm.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        boolean mustChangePassword
) {}
