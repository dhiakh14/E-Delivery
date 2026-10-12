package tn.esprit.examen.nomPrenomClasseExamen.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.idUser <> :idUser")
    List<User> findAllExcept(@Param("idUser") Long idUser);

    // Comptage des comptes actifs
    long countByEnabledTrueAndAccountLockedFalse();

    // Comptage des comptes désactivés
    long countByEnabledFalse();

    // Comptage des comptes verrouillés
    long countByAccountLockedTrue();

    // Comptage des comptes avec face embedding
    long countByFaceEmbeddingIsNotNull();

    // Utilisateurs créés après une date
    List<User> findByCreateDateAfter(LocalDateTime date);

    // Comptage des utilisateurs créés après une date
    long countByCreateDateAfter(LocalDateTime date);

    // Statistiques par statut
    @Query("SELECT " +
            "SUM(CASE WHEN u.enabled = true AND u.accountLocked = false THEN 1 ELSE 0 END) as active, " +
            "SUM(CASE WHEN u.enabled = false THEN 1 ELSE 0 END) as disabled, " +
            "SUM(CASE WHEN u.accountLocked = true THEN 1 ELSE 0 END) as locked " +
            "FROM User u")
    Object[] getAccountStatusSummary();

    // Statistiques par mois
    @Query("SELECT FUNCTION('YEAR', u.createDate) as year, " +
            "FUNCTION('MONTH', u.createDate) as month, " +
            "COUNT(u) as count " +
            "FROM User u " +
            "GROUP BY FUNCTION('YEAR', u.createDate), FUNCTION('MONTH', u.createDate) " )
    List<Object[]> getMonthlyRegistrationStats();
}

