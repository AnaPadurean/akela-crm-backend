package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findFirstByNameNorm(String nameNorm);
    long countByActiveTrue();

}
