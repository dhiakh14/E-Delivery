package tn.esprit.examen.nomPrenomClasseExamen.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "incident")
@NoArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaintenance;


    private String refCode;
    @Enumerated(EnumType.STRING)
    private StatusMaintenace statusMaintenance;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "incident_id")
    private Incident incident;

}
