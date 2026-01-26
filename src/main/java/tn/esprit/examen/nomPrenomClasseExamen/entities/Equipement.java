package tn.esprit.examen.nomPrenomClasseExamen.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    private String nomEquipement;
    private String refEquipement;
    @Enumerated(EnumType.STRING)
    private StatusEquipement statusEquipement;

    private String serialNumber;

    @ManyToOne
    @JsonIgnoreProperties("equipements")
    @JoinColumn(name = "categorie_id")
    private CategorieEquipement categorie;  // lien vers catégorie


    @ManyToOne
    @JoinColumn(name = "site_id")
    private Sites site;
}
