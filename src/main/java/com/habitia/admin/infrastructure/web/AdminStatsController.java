package com.habitia.admin.infrastructure.web;

import com.habitia.admin.application.AdminStatsResponse;
import com.habitia.admin.application.GetAdminStatsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final GetAdminStatsUseCase getAdminStats;

    public AdminStatsController(GetAdminStatsUseCase getAdminStats) {
        this.getAdminStats = getAdminStats;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(getAdminStats.execute());
    }
}
