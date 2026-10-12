package tn.esprit.examen.nomPrenomClasseExamen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class FactureResponse {
    private Long id;
    private String numeroFacture;
    private Double montantTotal;
    private String statut;
    private Long commandeId;
}
