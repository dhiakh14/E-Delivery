package tn.esprit.examen.nomPrenomClasseExamen.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.entities.User;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserRepository userRepository;

    /**
     * Statistiques générales des comptes
     */
    public Map<String, Object> getAccountStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalAccounts = userRepository.count();
        long activeAccounts = userRepository.countByEnabledTrueAndAccountLockedFalse();
        long disabledAccounts = userRepository.countByEnabledFalse();
        long lockedAccounts = userRepository.countByAccountLockedTrue();

        stats.put("totalAccounts", totalAccounts);
        stats.put("activeAccounts", activeAccounts);
        stats.put("disabledAccounts", disabledAccounts);
        stats.put("lockedAccounts", lockedAccounts);

        // Calcul des pourcentages
        if (totalAccounts > 0) {
            stats.put("activePercentage", (activeAccounts * 100.0) / totalAccounts);
            stats.put("disabledPercentage", (disabledAccounts * 100.0) / totalAccounts);
            stats.put("lockedPercentage", (lockedAccounts * 100.0) / totalAccounts);
        }

        return stats;
    }

    /**
     * Statistiques détaillées pour dashboard
     */
    public Map<String, Object> getDetailedStatistics() {
        Map<String, Object> detailedStats = new HashMap<>();

        // Statistiques de base
        detailedStats.put("basicStats", getAccountStatistics());

        // Évolution des créations de comptes (30 derniers jours) - SANS ChronoUnit
        detailedStats.put("accountCreationTrend", getAccountCreationTrend());

        // Répartition par rôle
        detailedStats.put("accountsByRole", getAccountsByRole());

        // Nouveaux comptes aujourd'hui
        detailedStats.put("newAccountsToday", getNewAccountsToday());

        // Comptes créés ce mois
        detailedStats.put("newAccountsThisMonth", getNewAccountsThisMonth());

        return detailedStats;
    }

    /**
     * Tendance de création de comptes - Version sans ChronoUnit
     */
    private Map<String, Long> getAccountCreationTrend() {
        Map<String, Long> trend = new HashMap<>();

        // Calculer la date d'il y a 30 jours manuellement
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);

        List<User> recentUsers = userRepository.findByCreateDateAfter(startDate);

        // Grouper par date
        trend = recentUsers.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getCreateDate().toLocalDate().toString(),
                        Collectors.counting()
                ));

        return trend;
    }

    /**
     * Répartition des comptes par rôle
     */
    private Map<String, Long> getAccountsByRole() {
        Map<String, Long> roleStats = new HashMap<>();

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if (user.getRoles() != null) {
                user.getRoles().forEach(role -> {
                    String roleName = role.getName();
                    roleStats.put(roleName, roleStats.getOrDefault(roleName, 0L) + 1);
                });
            }
        }

        return roleStats;
    }

    /**
     * Nouveaux comptes aujourd'hui
     */
    private long getNewAccountsToday() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        return userRepository.countByCreateDateAfter(startOfDay);
    }

    /**
     * Nouveaux comptes ce mois
     */
    private long getNewAccountsThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay();
        return userRepository.countByCreateDateAfter(startOfMonth);
    }

    /**
     * Statistiques spécifiques pour WIFAK BANK
     */
    public Map<String, Object> getWifakBankStats() {
        Map<String, Object> wifakStats = new HashMap<>();

        wifakStats.put("bankName", "WIFAK BANK");
        wifakStats.put("reportDate", LocalDateTime.now());
        wifakStats.put("statistics", getDetailedStatistics());

        // Ajouter des métriques spécifiques
        wifakStats.put("accountsWithoutRole", getAccountsWithoutRole());
        wifakStats.put("accountsWithFaceEmbedding", countAccountsWithFaceEmbedding());

        return wifakStats;
    }

    /**
     * Comptes sans rôle
     */
    private long getAccountsWithoutRole() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles() == null || user.getRoles().isEmpty())
                .count();
    }

    /**
     * Comptes avec embedding facial
     */
    private long countAccountsWithFaceEmbedding() {
        return userRepository.countByFaceEmbeddingIsNotNull();
    }
}