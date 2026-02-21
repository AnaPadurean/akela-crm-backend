package com.example.akela.swim.crm.auth;

import com.example.akela.swim.crm.user.AklUser;
import com.example.akela.swim.crm.user.AklUserRepository;
import com.example.akela.swim.crm.user.UserRole;
import com.example.akela.swim.crm.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapAdminRunner implements CommandLineRunner {

    private final AklUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${akl.bootstrap.admin.email}")
    private String adminEmail;

    @Value("${akl.bootstrap.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        AklUser admin = AklUser.builder()
                .email(adminEmail.trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(adminPassword))
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .coachId(null)
                .build();

        userRepository.save(admin);

        System.out.println("Bootstrap admin created: " + admin.getEmail());
    }
}
