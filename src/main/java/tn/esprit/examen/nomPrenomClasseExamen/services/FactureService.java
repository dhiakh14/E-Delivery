package tn.esprit.examen.nomPrenomClasseExamen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.examen.nomPrenomClasseExamen.dto.FactureResponse;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Commande;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Facture;
import tn.esprit.examen.nomPrenomClasseExamen.enums.StatutFacture;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.FactureRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FactureService {
    private final FactureRepository factureRepository;



    public Facture genererFacture(Commande commande) {

        if (commande.getFacture() != null) {
            return commande.getFacture();
        }

        Facture facture = Facture.builder()
                .numeroFacture(genererNumero())
                .dateFacture(new Date())
                .montantTotal(commande.getMontantTotal())
                .statut(StatutFacture.IMPAYEE)
                .commande(commande)
                .build();

        commande.setFacture(facture);
        return factureRepository.save(facture);
    }

    public FactureResponse marquerPayee(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(()-> new RuntimeException("Facture introuvable : " + factureId));


        facture.setStatut(StatutFacture.PAYEE);
        return toResponse(factureRepository.save(facture));
    }

    @Transactional(readOnly = true)
    public FactureResponse findByNumero(String numero) {
        return factureRepository.findByNumeroFacture(numero)
                .map(this::toResponse)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public FactureResponse findByCommande(Long commandeId) {
        return factureRepository.findByCommandeId(commandeId)
                .map(this::toResponse)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<FactureResponse> findAll() {
        return factureRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private String genererNumero() {
        String numero;
        do {
            numero = String.format("FAC-%d-%06d",
                    LocalDate.now().getYear(),
                    ThreadLocalRandom.current().nextInt(1, 1_000_000));
        } while (factureRepository.findByNumeroFacture(numero).isPresent());
        return numero;
    }

    private FactureResponse toResponse(Facture f) {
        return FactureResponse.builder()
                .id(f.getId())
                .numeroFacture(f.getNumeroFacture())
                .montantTotal(f.getMontantTotal())
                .statut(f.getStatut().name())
                .commandeId(f.getCommande() != null ? f.getCommande().getId() : null)
                .build();
    }
}
