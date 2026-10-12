package tn.esprit.examen.nomPrenomClasseExamen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class LigneCommandeRequest {

    @NotNull
    private Long foodId;

    @NotNull
    @Min(1)
    private Integer quantite;

    public LigneCommandeRequest() {
    }

    public LigneCommandeRequest(Long foodId, Integer quantite) {
        this.foodId = foodId;
        this.quantite = quantite;
    }
}
