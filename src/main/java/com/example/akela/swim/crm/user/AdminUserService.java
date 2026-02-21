package com.example.akela.swim.crm.user;

import com.example.akela.swim.crm.auth.dto.CreateCoachUserRequest;
import com.example.akela.swim.crm.auth.dto.CreateCoachUserResponse;
import com.example.akela.swim.crm.entity.CoachEntity;
import com.example.akela.swim.crm.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AklUserRepository userRepository;
    private final CoachRepository coachRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateCoachUserResponse createCoachUser(CreateCoachUserRequest req) {
        String email = req.email().trim().toLowerCase();

        CoachEntity coach = coachRepository.findById(req.coachId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Antrenorul nu a foost gasit!"));

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Emailul a fost deja folosit!");
        }

        if (userRepository.existsByCoachId(coach.getCoachId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Antrenorul are deja un cont!");
        }

        if (req.password().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parola trebuie să aibă cel putin 8 caractere!");
        }

        UserStatus status = req.forceChangePassword() ? UserStatus.PENDING_PASSWORD : UserStatus.ACTIVE;

        AklUser user = AklUser.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(UserRole.COACH)
                .status(status)
                .coachId(coach.getCoachId())
                .build();

        AklUser saved = userRepository.save(user);

        return new CreateCoachUserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.getStatus().name(),
                saved.getCoachId()
        );
    }
}
