package tn.esprit.examen.nomPrenomClasseExamen.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CommandeResponse {
    private Long id;
    private String statut;
    private Double montantTotal;
    private String adresseLivraison;
    private String restaurantNom;
    private List<LigneDto> lignes;

}
