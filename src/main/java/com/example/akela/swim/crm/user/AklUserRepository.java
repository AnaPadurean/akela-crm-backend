package com.example.akela.swim.crm.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AklUserRepository extends JpaRepository<AklUser, Long> {
    Optional<AklUser> findByEmailIgnoreCase(String email);
    boolean existsByRole(UserRole role);
    boolean existsByCoachId(Long coachId);
    boolean existsByEmailIgnoreCase(String email);
    List<AklUser> findAllByRole(UserRole role);

    @Query("select u from AklUser u where u.role = 'COACH' and u.coachId is not null")
    List<AklUser> findAllCoachUsers();
}
