package tn.esprit.examen.nomPrenomClasseExamen.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import tn.esprit.examen.nomPrenomClasseExamen.enums.StatutCommande;
import tn.esprit.examen.nomPrenomClasseExamen.enums.StatutFacture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Builder

public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String numeroFacture;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateFacture;

    @Column(nullable = false)
    private Double montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutFacture statut = StatutFacture.IMPAYEE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"facture", "lignes", "hibernateLazyInitializer", "handler"})
    private Commande commande;

    public Facture() {
    }

    public Facture(Long id, String numeroFacture, Date dateFacture, Double montantTotal, StatutFacture statut, Commande commande) {
        this.id = id;
        this.numeroFacture = numeroFacture;
        this.dateFacture = dateFacture;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.commande = commande;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public StatutFacture getStatut() {
        return statut;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}
