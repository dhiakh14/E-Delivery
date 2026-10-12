package tn.esprit.examen.nomPrenomClasseExamen.services;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Food;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Menu;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Restaurant;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.FoodRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.MenuRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.ResataurentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class FoodService {
    private final FoodRepository foodRepository;
    private final ResataurentRepository restaurantRepository;

    @Transactional(readOnly = true)
    public List<Food> findByRestaurant(Long restaurantId, boolean seulementDisponibles) {
        return seulementDisponibles
                ? foodRepository.findByRestaurantIdAndDisponibleTrue(restaurantId)
                : foodRepository.findByRestaurantId(restaurantId);
    }

    @Transactional(readOnly = true)
    public Food findById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow();
    }

    public Food create(Long restaurantId, Food food) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        food.setId(null);
        food.setRestaurant(restaurant);
        if (food.getDisponible() == null) food.setDisponible(true);
        return foodRepository.save(food);
    }

    public Food update(Long id, Food data) {
        Food food = findById(id);
        food.setNom(data.getNom());
        food.setDescription(data.getDescription());
        food.setPrix(data.getPrix());
        food.setImage(data.getImage());
        if (data.getDisponible() != null) food.setDisponible(data.getDisponible());
        return foodRepository.save(food);
    }

    public Food toggleDisponible(Long id) {
        Food food = findById(id);
        food.setDisponible(!Boolean.TRUE.equals(food.getDisponible()));
        return foodRepository.save(food);
    }

    public void delete(Long id) {
        foodRepository.delete(findById(id));
    }
}
