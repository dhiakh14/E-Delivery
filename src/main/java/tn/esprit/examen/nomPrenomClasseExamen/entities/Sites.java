package tn.esprit.examen.nomPrenomClasseExamen.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @OneToMany(mappedBy = "sites", cascade = CascadeType.ALL)
    private Set<Incident> lincident;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
    private Set<Equipement> equipements;


}
