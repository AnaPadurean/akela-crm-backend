package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.dto.CoachListDTO;
import com.example.akela.swim.crm.dto.CoachOptionDto;
import com.example.akela.swim.crm.dto.CreateUpdateCoachDTO;
import com.example.akela.swim.crm.entity.CoachEntity;
import com.example.akela.swim.crm.entity.CoachLocationAssocEntity;
import com.example.akela.swim.crm.entity.LocationEntity;
import com.example.akela.swim.crm.repository.CoachLocationAssocRepository;
import com.example.akela.swim.crm.repository.CoachRepository;
import com.example.akela.swim.crm.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final LocationRepository locationRepository;
    private final CoachLocationAssocRepository coachLocationAssocRepository;

    public CoachService(CoachRepository coachRepository,
                        LocationRepository locationRepository,
                        CoachLocationAssocRepository coachLocationAssocRepository) {
        this.coachRepository = coachRepository;
        this.locationRepository = locationRepository;
        this.coachLocationAssocRepository = coachLocationAssocRepository;
    }

    // LISTARE “flattened” pentru UI (evită circular/LAZY issues)
    @Transactional(readOnly = true)
    public List<CoachListDTO> findAllFlattened() {
        List<CoachEntity> coaches = coachRepository.findAll();
        List<Long> coachIds = coaches.stream().map(CoachEntity::getCoachId).toList();

        Map<Long, List<Long>> locationIdsByCoachId = coachIds.isEmpty()
                ? Map.of()
                : coachLocationAssocRepository.findByCoach_CoachIdIn(coachIds)
                .stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCoach().getCoachId(),
                        Collectors.mapping(a -> a.getLocation().getLocationId(), Collectors.toList())
                ));

        List<CoachListDTO> result = new ArrayList<>();
        for (CoachEntity c : coaches) {
            result.add(new CoachListDTO(
                    c.getCoachId(),
                    c.getCoachFirstName(),
                    c.getCoachLastName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getAddress(),
                    locationIdsByCoachId.getOrDefault(c.getCoachId(), List.of())
            ));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<CoachEntity> findById(Long id) {
        return coachRepository.findById(id);
    }

    @Transactional
    public CoachEntity createCoach(CreateUpdateCoachDTO dto) {
        CoachEntity coach = new CoachEntity();
        coach.setCoachFirstName(dto.getCoachFirstName());
        coach.setCoachLastName(dto.getCoachLastName());
        coach.setPhone(dto.getPhone());
        coach.setEmail(dto.getEmail());
        coach.setAddress(dto.getAddress());

        coach = coachRepository.save(coach);

        upsertLocationsForCoach(coach, dto.getLocationIds());

        return coach;
    }

    @Transactional
    public CoachEntity updateCoach(Long coachId, CreateUpdateCoachDTO dto) {
        CoachEntity coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        coach.setCoachFirstName(dto.getCoachFirstName());
        coach.setCoachLastName(dto.getCoachLastName());
        coach.setPhone(dto.getPhone());
        coach.setEmail(dto.getEmail());
        coach.setAddress(dto.getAddress());

        coachRepository.save(coach);

        // reset + recreate assocs
        coachLocationAssocRepository.deleteByCoach_CoachId(coachId);
        upsertLocationsForCoach(coach, dto.getLocationIds());

        return coach;
    }

    private void upsertLocationsForCoach(CoachEntity coach, List<Long> locationIds) {
        if (locationIds == null) return;

        for (Long locationId : locationIds) {
            LocationEntity location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Location not found: " + locationId));

            // (optional) protecție în plus dacă nu ai unique constraint încă:
            if (coachLocationAssocRepository.existsByCoach_CoachIdAndLocation_LocationId(
                    coach.getCoachId(), locationId)) {
                continue;
            }

            CoachLocationAssocEntity assoc = new CoachLocationAssocEntity();
            assoc.setCoach(coach);
            assoc.setLocation(location);
            assoc.setActive(true);

            coachLocationAssocRepository.save(assoc);
        }
    }

    @Transactional
    public void delete(Long id) {
        coachLocationAssocRepository.deleteByCoach_CoachId(id);
        coachRepository.deleteById(id);
    }

    // util: coachi dintr-o locație
    @Transactional(readOnly = true)
    public List<CoachListDTO> findByLocationId(Long locationId) {
        List<CoachLocationAssocEntity> assocs = coachLocationAssocRepository.findByLocation_LocationId(locationId);

        Map<Long, List<Long>> locationIdsByCoachId = assocs.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCoach().getCoachId(),
                        Collectors.mapping(a -> a.getLocation().getLocationId(), Collectors.toList())
                ));

        Map<Long, CoachEntity> coachById = assocs.stream()
                .map(CoachLocationAssocEntity::getCoach)
                .collect(Collectors.toMap(CoachEntity::getCoachId, c -> c, (a, b) -> a));

        return coachById.values().stream()
                .map(c -> new CoachListDTO(
                        c.getCoachId(),
                        c.getCoachFirstName(),
                        c.getCoachLastName(),
                        c.getPhone(),
                        c.getEmail(),
                        c.getAddress(),
                        locationIdsByCoachId.getOrDefault(c.getCoachId(), List.of())
                ))
                .toList();
    }

    public List<CoachOptionDto> getCoachOptions() {
        return coachRepository.findAll().stream()
                .map(c -> new CoachOptionDto(
                        c.getCoachId(),
                        c.getCoachFirstName() + " " + c.getCoachLastName(),
                        c.getEmail()
                ))
                .toList();
    }

    public long getTotalCount() {
        return coachRepository.count();
    }

}
