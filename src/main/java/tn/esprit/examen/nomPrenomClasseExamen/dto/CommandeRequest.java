package tn.esprit.examen.nomPrenomClasseExamen.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data

public class CommandeRequest {
    @NotNull
    private Long clientId;

    @NotNull
    private Long restaurantId;

    @NotBlank
    private String adresseLivraison;

    @NotEmpty
    @Valid
    private List<LigneCommandeRequest> lignes;

    public CommandeRequest() {
    }

    public CommandeRequest(Long clientId, Long restaurantId, String adresseLivraison, List<LigneCommandeRequest> lignes) {
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.adresseLivraison = adresseLivraison;
        this.lignes = lignes;
    }
}
