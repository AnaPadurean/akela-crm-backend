package com.example.akela.swim.crm.auth.reset;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "akl_password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="token_hash", nullable=false, length=64, unique=true)
    private String tokenHash;

    @Column(name="expires_at", nullable=false)
    private OffsetDateTime expiresAt;

    @Column(name="used_at")
    private OffsetDateTime usedAt;

    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}
