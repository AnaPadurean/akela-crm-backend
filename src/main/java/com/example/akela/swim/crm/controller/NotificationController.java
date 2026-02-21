package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.NotificationDTO;
import com.example.akela.swim.crm.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/warnings")
    public ResponseEntity<List<NotificationDTO>> warnings() {
        return ResponseEntity.ok(notificationService.getWarnings());
    }
}
