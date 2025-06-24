package com.example.lib.controller;

import com.example.lib.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getCustomerDashboard(id));
    }
}
