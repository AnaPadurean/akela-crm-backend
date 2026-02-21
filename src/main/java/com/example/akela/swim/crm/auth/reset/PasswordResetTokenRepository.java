package com.example.akela.swim.crm.auth.reset;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findFirstByTokenHash(String tokenHash);
    void deleteByUserId(Long userId);
}
