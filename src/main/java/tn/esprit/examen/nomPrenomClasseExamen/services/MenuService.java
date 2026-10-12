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

public class MenuService {
    private final MenuRepository menuRepository;
    private final ResataurentRepository restaurantRepository;
    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public List<Menu> findByRestaurant(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @Transactional(readOnly = true)
    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow();
    }

    public Menu create(Long restaurantId, Menu menu, List<Long> foodIds) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        menu.setId(null);
        menu.setRestaurant(restaurant);
        menu.setFoods(resoudreFoods(foodIds, restaurantId));
        return menuRepository.save(menu);
    }

    public Menu update(Long id, Menu data, List<Long> foodIds) {
        Menu menu = findById(id);
        menu.setNom(data.getNom());
        menu.setDescription(data.getDescription());
        if (foodIds != null) {
            menu.setFoods(resoudreFoods(foodIds, menu.getRestaurant().getId()));
        }
        return menuRepository.save(menu);
    }

    public void delete(Long id) {
        menuRepository.delete(findById(id));
    }

    private List<Food> resoudreFoods(List<Long> foodIds, Long restaurantId) {
        List<Food> foods = new ArrayList<>();
        if (foodIds == null) return foods;

        for (Long foodId : foodIds) {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));
            if (!food.getRestaurant().getId().equals(restaurantId)) {
                throw new RuntimeException("Food with id: " + foodId + " does not belong to restaurant with id: " + restaurantId);
            }
            foods.add(food);
        }
        return foods;
    }
}
