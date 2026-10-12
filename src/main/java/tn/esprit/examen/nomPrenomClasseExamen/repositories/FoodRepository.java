package tn.esprit.examen.nomPrenomClasseExamen.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Food;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByRestaurantId(Long restaurantId);
    List<Food> findByRestaurantIdAndDisponibleTrue(Long restaurantId);

    @Query("SELECT f FROM Food f JOIN f.menus m WHERE m.id = :menuId")
    List<Food> findByMenuId(@Param("menuId") Long menuId);
}