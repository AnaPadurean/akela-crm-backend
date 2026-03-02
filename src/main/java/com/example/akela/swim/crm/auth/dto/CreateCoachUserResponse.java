package com.example.akela.swim.crm.auth.dto;

public record CreateCoachUserResponse(
        long userId,
        String email,
        String role,
        String status,
        Long coachId
) {
}
