package tn.esprit.examen.nomPrenomClasseExamen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Restaurant;

import java.util.List;

@Repository
public interface ResataurentRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findByOuvertTrue();
    List<Restaurant> findByNomContainingIgnoreCase(String nom);
}
