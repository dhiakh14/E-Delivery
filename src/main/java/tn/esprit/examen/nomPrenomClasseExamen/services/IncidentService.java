package tn.esprit.examen.nomPrenomClasseExamen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Equipement;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Incident;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Maintenance;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Sites;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.EquipementRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.IncidentRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.MaintenanceRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.SitesRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final SitesRepository sitesRepository;
    private final EquipementRepository equipementRepository;

    //Créer incident + maintenances
    public Incident createIncidentWithMaintenances(
            Incident incident,
            List<Maintenance> maintenances) {

        for (Maintenance m : maintenances) {
            incident.addMaintenance(m); // liaison bidirectionnelle
        }

        return incidentRepository.save(incident);
    }

    // Affecter une maintenance existante à un incident existant
    public Maintenance affectMaintenanceToIncident(
            Long idMaintenance,
            Long idIncident) {

        Maintenance maintenance = maintenanceRepository.findById(idMaintenance)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        Incident incident = incidentRepository.findById(idIncident)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        maintenance.setIncident(incident);

        return maintenanceRepository.save(maintenance);
    }

    //Depuis Incident → voir Maintenances
    public Set<Maintenance> getMaintenancesByIncident(Long idIncident) {
        Incident incident = incidentRepository.findById(idIncident)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        return incident.getLmaintenance();
    }

    // Depuis Maintenance → voir Incident
    public Incident getIncidentByMaintenance(Long idMaintenance) {
        Maintenance maintenance = maintenanceRepository.findById(idMaintenance)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        return maintenance.getIncident();
    }

    public Incident affecterIncidentToSite(Long incidentId, Long siteId) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident introuvable"));

        Sites site = sitesRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site introuvable"));

        // 🔥 AFFECTATION AVANT SAVE
        incident.setSites(site);

        return incidentRepository.save(incident);
    }
/*
    public Incident creerIncidentComplet(
           Incident incident,
            Long siteId,
            Set<Long> equipementIds) {

        //  Récupérer le site existant
        Sites site = sitesRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site introuvable"));

        //  Récupérer les équipements existants
        Set<Equipement> equipements = new HashSet<>(
                equipementRepository.findAllById(equipementIds)
        );


        incident.setSites(site);
        site.setEquipements(equipements);
        sitesRepository.save(site);


        return incidentRepository.save(incident);
    }*/
}
