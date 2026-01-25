package tn.esprit.examen.nomPrenomClasseExamen.entities;

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
}
