package com.example.akela.swim.crm.user;

import com.example.akela.swim.crm.user.UserStatus;

import java.time.OffsetDateTime;

public record AdminCoachUserDto(
        Long userId,
        Long coachId,
        String label,
        String email,
        UserStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime lastLoginAt
) {}
