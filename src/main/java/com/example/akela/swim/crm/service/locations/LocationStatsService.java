package com.example.akela.swim.crm.service.locations;

import com.example.akela.swim.crm.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationStatsService {

    private final LocationRepository locationRepository;

    public LocationStatsService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public long getActiveLocationsCount() {
        return locationRepository.countByActiveTrue();
    }
}
