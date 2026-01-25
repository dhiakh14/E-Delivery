package tn.esprit.examen.nomPrenomClasseExamen.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Sites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSite;

    private String codeRef;
    private String nom;
    private String countryCode;
    private String currencyCode;
    @Enumerated(EnumType.STRING)
    private StatusSites statusSites;

}
