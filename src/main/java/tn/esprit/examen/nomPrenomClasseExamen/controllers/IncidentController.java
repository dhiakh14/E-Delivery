package tn.esprit.examen.nomPrenomClasseExamen.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Incident;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Maintenance;
import tn.esprit.examen.nomPrenomClasseExamen.services.IncidentService;

import java.util.ArrayList;
import java.util.Set;

@RestController
@RequestMapping("/api/incidents")
@Tag(name = "Incident", description = "Gestion des incidents et maintenances")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;

    //Créer un incident avec ses maintenances
    @PostMapping("/createIncidentWithMaintenances")
    public Incident createIncidentWithMaintenances(
            @RequestBody Incident incident) {

        return incidentService.createIncidentWithMaintenances(
                incident,
                new ArrayList<>(incident.getLmaintenance())
        );
    }

    //Affecter une maintenance existante à un incident existant
    @PutMapping("/affectMaintenanceToIncident/{incidentId}/maintenance/{maintenanceId}")
    public Maintenance affectMaintenanceToIncident(
            @PathVariable Long incidentId,
            @PathVariable Long maintenanceId) {

        return incidentService.affectMaintenanceToIncident(
                maintenanceId,
                incidentId
        );
    }

    //Depuis Incident on peut voir Maintenances
    @GetMapping("/getMaintenancesByIncident/{incidentId}/maintenances")
    public Set<Maintenance> getMaintenancesByIncident(
            @PathVariable Long incidentId) {

        return incidentService.getMaintenancesByIncident(incidentId);
    }

    //Depuis Maintenance on peut voir Incident
    @GetMapping("/getIncidentByMaintenance/maintenance/{maintenanceId}/incident")
    public Incident getIncidentByMaintenance(
            @PathVariable Long maintenanceId) {

        return incidentService.getIncidentByMaintenance(maintenanceId);
    }



    /**
     * Crée un incident complet, l'affecte à un site existant
     * et déclare les équipements défectueux.
     *
     * @param incident     L'objet Incident envoyé depuis le body
     * @param siteId       ID du site existant
     * @param equipementIds Liste d'IDs des équipements défectueux
     * @return L'incident créé
     */
  /*  @PostMapping("/creerIncident")
    public Incident creerIncident(
            @RequestBody Incident incident,
            @RequestParam Long siteId,
            @RequestParam Set<Long> equipementIds) {

        return incidentService.creerIncidentComplet(incident, siteId, equipementIds);
    }*/
}
