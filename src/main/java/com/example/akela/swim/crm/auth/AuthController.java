package com.example.akela.swim.crm.auth;

import com.example.akela.swim.crm.auth.dto.*;
import com.example.akela.swim.crm.auth.reset.PasswordResetService;
import com.example.akela.swim.crm.security.AklUserPrincipal;
import com.example.akela.swim.crm.security.JwtService;
import com.example.akela.swim.crm.user.AklUser;
import com.example.akela.swim.crm.user.AklUserRepository;
import com.example.akela.swim.crm.user.UserStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AklUserRepository userRepository;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        String email = req.email().trim().toLowerCase();

        UserDetails ud = userDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(req.password(), ud.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        AklUserPrincipal principal = (AklUserPrincipal) ud;

        if (principal.getStatus() == com.example.akela.swim.crm.user.UserStatus.DISABLED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account disabled");
        }

        String token = jwtService.generateToken(principal);

        boolean mustChange = principal.getStatus() == com.example.akela.swim.crm.user.UserStatus.PENDING_PASSWORD;

        return ResponseEntity.ok(
                new LoginResponse(token, "Bearer", jwtService.expirationSeconds(), mustChange)
        );
    }


    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        AklUserPrincipal p = (AklUserPrincipal) authentication.getPrincipal();

        Map<String, Object> res = new java.util.HashMap<>();
        res.put("userId", p.getUserId());
        res.put("email", p.getEmail());
        res.put("role", p.getRole());
        res.put("status", p.getStatus().name());
        res.put("mustChangePassword", p.getStatus() == com.example.akela.swim.crm.user.UserStatus.PENDING_PASSWORD);
        if (p.getCoachId() != null) {
            res.put("coachId", p.getCoachId());
        } else {
            res.put("coachId", null);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal AklUserPrincipal principal,
            @RequestBody @Valid ChangePasswordRequest req
    ) {
        if (req.newPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parola trebuie sa aiba cel putin 8 caractere!");
        }

        AklUser user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(req.currentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Parola curenta este gresita!");
        }

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest req) {
        passwordResetService.requestReset(req.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest req) {
        passwordResetService.resetPassword(req.token(), req.newPassword());
        return ResponseEntity.noContent().build();
    }
}

