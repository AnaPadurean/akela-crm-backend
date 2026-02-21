package com.example.akela.swim.crm.user;

import com.example.akela.swim.crm.entity.CoachEntity;
import com.example.akela.swim.crm.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/coach-users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCoachUsersController {

    private final AklUserRepository userRepository;
    private final CoachRepository coachRepository;

    @GetMapping
    public List<AdminCoachUserDto> listCoachUsers() {

        var users = userRepository.findAllCoachUsers();

        // 1) colectăm coachIds (fără null)
        Set<Long> coachIds = users.stream()
                .map(AklUser::getCoachId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2) luăm coachii într-un singur query și facem map
        Map<Long, CoachEntity> coachById = coachIds.isEmpty()
                ? Map.of()
                : coachRepository.findAllByCoachIdIn(coachIds).stream()
                .collect(Collectors.toMap(CoachEntity::getCoachId, Function.identity()));

        // 3) construim DTO
        return users.stream()
                .map(u -> new AdminCoachUserDto(
                        u.getId(),
                        u.getCoachId(),
                        buildLabel(u, coachById),
                        u.getEmail(),
                        u.getStatus(),
                        u.getCreatedAt(),
                        u.getLastLoginAt()
                ))
                .toList();
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> setStatus(
            @PathVariable Long userId,
            @RequestBody SetStatusRequest req
    ) {
        AklUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != UserRole.COACH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only coach accounts can be managed here");
        }

        if (req.status() == UserStatus.DISABLED) {
            user.setStatus(UserStatus.DISABLED);
        } else if (req.status() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.ACTIVE);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
        }

        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCoachUser(@PathVariable Long userId) {
        AklUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != UserRole.COACH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only coach accounts can be deleted here");
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    private String buildLabel(AklUser u, Map<Long, CoachEntity> coachById) {
        if (u.getCoachId() == null) return u.getEmail();

        CoachEntity c = coachById.get(u.getCoachId());
        if (c == null) return u.getEmail();

        String first = Optional.ofNullable(c.getCoachFirstName()).orElse("").trim();
        String last  = Optional.ofNullable(c.getCoachLastName()).orElse("").trim();
        String full = (first + " " + last).trim();

        return full.isEmpty() ? u.getEmail() : full;
    }

    public record SetStatusRequest(UserStatus status) {}
}
