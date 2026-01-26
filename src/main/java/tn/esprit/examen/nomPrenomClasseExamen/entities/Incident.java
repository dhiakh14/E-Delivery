package tn.esprit.examen.nomPrenomClasseExamen.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "lmaintenance")

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

    @OneToMany(mappedBy = "incident",cascade = CascadeType.ALL)
    private Set<Maintenance> lmaintenance;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sites_id")
    private Sites sites;


    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL)
    private Set<Maintenance> maintenances;


    /***************Fonction Helper pour les affections *****************************************/
    public void addMaintenance(Maintenance m) {
        lmaintenance.add(m);
        m.setIncident(this);
    }

}
