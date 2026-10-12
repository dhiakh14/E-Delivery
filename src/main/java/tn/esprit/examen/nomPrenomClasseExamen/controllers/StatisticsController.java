package tn.esprit.examen.nomPrenomClasseExamen.controllers;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.nomPrenomClasseExamen.services.StatisticsService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/accounts/summary")
    public ResponseEntity<Map<String, Object>> getAccountSummary() {
        return ResponseEntity.ok(statisticsService.getAccountStatistics());
    }

    @GetMapping("/accounts/detailed")
    public ResponseEntity<Map<String, Object>> getDetailedStats() {
        return ResponseEntity.ok(statisticsService.getDetailedStatistics());
    }

    @GetMapping("/wifak-bank/dashboard")
    public ResponseEntity<Map<String, Object>> getWifakBankDashboard() {
        return ResponseEntity.ok(statisticsService.getWifakBankStats());
    }

    @GetMapping("/accounts/active")
    public ResponseEntity<Map<String, Object>> getActiveAccountsStats() {
        Map<String, Object> stats = statisticsService.getAccountStatistics();
        return ResponseEntity.ok(Map.of(
                "activeAccounts", stats.get("activeAccounts"),
                "activePercentage", stats.get("activePercentage")
        ));
    }

    @GetMapping("/accounts/disabled")
    public ResponseEntity<Map<String, Object>> getDisabledAccountsStats() {
        Map<String, Object> stats = statisticsService.getAccountStatistics();
        return ResponseEntity.ok(Map.of(
                "disabledAccounts", stats.get("disabledAccounts"),
                "disabledPercentage", stats.get("disabledPercentage")
        ));
    }
}