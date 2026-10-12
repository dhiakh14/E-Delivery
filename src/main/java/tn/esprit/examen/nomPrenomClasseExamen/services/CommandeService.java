package tn.esprit.examen.nomPrenomClasseExamen.services;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.dto.CommandeRequest;
import tn.esprit.examen.nomPrenomClasseExamen.dto.CommandeResponse;
import tn.esprit.examen.nomPrenomClasseExamen.dto.LigneCommandeRequest;
import tn.esprit.examen.nomPrenomClasseExamen.dto.LigneDto;
import tn.esprit.examen.nomPrenomClasseExamen.entities.*;
import tn.esprit.examen.nomPrenomClasseExamen.enums.StatutCommande;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final FoodRepository foodRepository;
    private final ResataurentRepository restaurantRepository;
    private final UserRepository userRepository;   // ton repository du module login
    private final FactureService factureService;

    /* ---------- SCÉNARIO 1 : passer une commande ---------- */
    public CommandeResponse passerCommande(CommandeRequest req) {

        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId())
                .orElseThrow();
        if (!Boolean.TRUE.equals(restaurant.getOuvert())) {

        }

        User client = userRepository.findById(req.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable : " + req.getClientId()));

        Commande commande = Commande.builder()
                .dateCommande(new Date())
                .statut(StatutCommande.EN_ATTENTE)
                .adresseLivraison(req.getAdresseLivraison())
                .client(client)
                .restaurant(restaurant)
                .montantTotal(0.0)
                .build();

        for (LigneCommandeRequest lr : req.getLignes()) {
            Food food = foodRepository.findById(lr.getFoodId())
                    .orElseThrow(()-> new RuntimeException("Plat introuvable : " + lr.getFoodId()));



            LigneCommande ligne = LigneCommande.builder()
                    .food(food)
                    .quantite(lr.getQuantite())
                    .prixUnitaire(food.getPrix())
                    .build();
            ligne.recalculerSousTotal();
            commande.ajouterLigne(ligne);
        }



        commande.recalculerMontantTotal();

        Commande saved = commandeRepository.save(commande);
        return toResponse(saved);
    }

    /* ---------- SCÉNARIO 2 : changer le statut ---------- */
    public CommandeResponse changerStatut(Long commandeId, StatutCommande nouveauStatut) {
        Commande commande = findEntity(commandeId);



        commande.setStatut(nouveauStatut);

        if (nouveauStatut == StatutCommande.LIVREE) {
            factureService.genererFacture(commande);
        }

        return toResponse(commandeRepository.save(commande));
    }

    /* ---------- SCÉNARIO 3 : annulation ---------- */
    public CommandeResponse annuler(Long commandeId) {
        Commande commande = findEntity(commandeId);


        commande.setStatut(StatutCommande.ANNULEE);
        return toResponse(commandeRepository.save(commande));
    }

    /* ---------- Lectures ---------- */
    @Transactional(readOnly = true)
    public Commande findEntity(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable : " + id));
    }

    @Transactional(readOnly = true)
    public CommandeResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> findByClient(Long clientId) {
        return commandeRepository.findByClientIdOrderByDateCommandeDesc(clientId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> findByRestaurantEtStatut(Long restaurantId, StatutCommande statut) {
        return commandeRepository.findByRestaurantIdAndStatut(restaurantId, statut)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /* ---------- Mapping ---------- */
    private CommandeResponse toResponse(Commande c) {
        return CommandeResponse.builder()
                .id(c.getId())
                .statut(c.getStatut().name())
                .montantTotal(c.getMontantTotal())
                .adresseLivraison(c.getAdresseLivraison())
                .restaurantNom(c.getRestaurant() != null ? c.getRestaurant().getNom() : null)
                .lignes(c.getLignes().stream()
                        .map(l -> LigneDto.builder()
                                .foodNom(l.getFood().getNom())
                                .quantite(l.getQuantite())
                                .prixUnitaire(l.getPrixUnitaire())
                                .sousTotal(l.getSousTotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
