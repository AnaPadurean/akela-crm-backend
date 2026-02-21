package com.example.akela.swim.crm.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


public record CreateCoachUserRequest(
        @NotNull Long coachId,
        @NotBlank @Email String email,
        @NotBlank String password,
        boolean forceChangePassword
) {}
