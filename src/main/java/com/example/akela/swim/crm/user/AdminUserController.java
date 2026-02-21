package com.example.akela.swim.crm.user;

import com.example.akela.swim.crm.auth.dto.CreateCoachUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/coach")
    public void createCoachUser(@RequestBody @Valid CreateCoachUserRequest req) {
        adminUserService.createCoachUser(req);
    }
}
