package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.CoachOptionDto;
import com.example.akela.swim.crm.service.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coaches")
@RequiredArgsConstructor
public class AdminCoachController {

    private final CoachService coachService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/options")
    public List<CoachOptionDto> getCoachOptions() {
        return coachService.getCoachOptions();
    }
}
