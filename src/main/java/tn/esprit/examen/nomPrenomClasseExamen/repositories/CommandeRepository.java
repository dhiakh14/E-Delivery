package tn.esprit.examen.nomPrenomClasseExamen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Commande;
import tn.esprit.examen.nomPrenomClasseExamen.enums.StatutCommande;

import java.util.List;

@Repository

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientIdOrderByDateCommandeDesc(Long clientId);
    List<Commande> findByRestaurantIdAndStatut(Long restaurantId, StatutCommande statut);
    List<Commande> findByStatut(StatutCommande statut);

}
