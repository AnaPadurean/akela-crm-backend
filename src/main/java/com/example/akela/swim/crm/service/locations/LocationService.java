package com.example.akela.swim.crm.service.locations;

import com.example.akela.swim.crm.entity.LocationEntity;
import com.example.akela.swim.crm.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository repo;

    public LocationService(LocationRepository repo) {
        this.repo = repo;
    }

    public List<LocationEntity> findAll() {
        return repo.findAll();
    }

    public Optional<LocationEntity> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<LocationEntity> findByNameNorm(String nameNorm) {
        return repo.findFirstByNameNorm(nameNorm);
    }

    public LocationEntity save(LocationEntity e) {
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public String normalizeName(String s) {
        return s == null ? "" : s.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
