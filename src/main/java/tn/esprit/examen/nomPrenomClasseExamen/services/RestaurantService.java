package tn.esprit.examen.nomPrenomClasseExamen.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Restaurant;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.ResataurentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional


public class RestaurantService {
    private final ResataurentRepository restaurantRepository;

    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Restaurant> findOuverts() {
        return restaurantRepository.findByOuvertTrue();
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    public Restaurant create(Restaurant r) {
        r.setId(null);
        if (r.getOuvert() == null) r.setOuvert(true);
        return restaurantRepository.save(r);
    }

    public Restaurant update(Long id, Restaurant data) {
        Restaurant r = findById(id);
        r.setNom(data.getNom());
        r.setAdresse(data.getAdresse());
        r.setTelephone(data.getTelephone());
        r.setDescription(data.getDescription());
        r.setImage(data.getImage());
        if (data.getOuvert() != null) r.setOuvert(data.getOuvert());
        return restaurantRepository.save(r);
    }

    public Restaurant toggleOuvert(Long id) {
        Restaurant r = findById(id);
        r.setOuvert(!Boolean.TRUE.equals(r.getOuvert()));
        return restaurantRepository.save(r);
    }

    public void delete(Long id) {
        restaurantRepository.delete(findById(id));
    }
}
