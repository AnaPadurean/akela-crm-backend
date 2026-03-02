package com.example.akela.swim.crm.auth.reset;

import com.example.akela.swim.crm.mail.MailService;
import com.example.akela.swim.crm.user.AklUser;
import com.example.akela.swim.crm.user.AklUserRepository;
import com.example.akela.swim.crm.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final AklUserRepository userRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.reset.token-ttl-minutes:30}")
    private long ttlMinutes;

    public void requestReset(String emailRaw) {
        String email = emailRaw.trim().toLowerCase();
        userRepo.findByEmailIgnoreCase(email).ifPresent(user -> {
            tokenRepo.deleteByUserId(user.getId());
            String rawToken = ResetTokenUtil.generateRawToken();
            String tokenHash = ResetTokenUtil.sha256Hex(rawToken);

            OffsetDateTime now = OffsetDateTime.now();
            PasswordResetToken t = PasswordResetToken.builder()
                    .userId(user.getId())
                    .tokenHash(tokenHash)
                    .createdAt(now)
                    .expiresAt(now.plusMinutes(ttlMinutes))
                    .build();

            tokenRepo.save(t);

            String link = frontendUrl + "/sessions/reset-password?token=" + rawToken;
            mailService.sendPasswordReset(user.getEmail(), link);
        });
    }

    public void resetPassword(String rawToken, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Parola trebuie sa aiba cel putin 8 caractere.");
        }

        String tokenHash = ResetTokenUtil.sha256Hex(rawToken);

        PasswordResetToken t = tokenRepo.findFirstByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Token invalid."));

        if (t.getUsedAt() != null) {
            throw new IllegalArgumentException("Token deja folosit.");
        }

        if (t.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Token expirat.");
        }

        AklUser user = userRepo.findById(t.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setStatus(UserStatus.ACTIVE);
        userRepo.save(user);

        t.setUsedAt(OffsetDateTime.now());
        tokenRepo.save(t);
    }
}
