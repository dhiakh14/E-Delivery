package tn.esprit.examen.nomPrenomClasseExamen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LigneDto {
    private String foodNom;
    private Integer quantite;
    private Double prixUnitaire;
    private Double sousTotal;
}
