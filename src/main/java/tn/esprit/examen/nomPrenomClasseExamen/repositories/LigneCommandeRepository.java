package tn.esprit.examen.nomPrenomClasseExamen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.LigneCommande;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {

}
