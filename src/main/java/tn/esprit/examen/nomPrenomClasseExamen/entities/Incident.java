package tn.esprit.examen.nomPrenomClasseExamen.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idInncident;

    private String severityCode;
    @Enumerated(EnumType.STRING)
    private EtatIncident etatIncident;
    @DateTimeFormat
    private Date date;
    private String description;
    private Double costEstimated;




}
