package com.soprasteria.flexOfficebackend.controller;

import com.soprasteria.flexOfficebackend.model.Bureau;
import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.service.AffectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

/* Ce contrôleur, gère les requêtes HTTP liées aux affectations.*/

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/affectations")
public class AffectationController {

    @Autowired
    private AffectationService affectationService;
    
    //Méthode qui gère la requête GET pour récupérer toutes les affectations existantes.
    @GetMapping
    public ResponseEntity<Map<LocalDate, Map<String, List<String>>>> getAffectations() {
        Map<LocalDate, Map<String, List<String>>> affectations = affectationService.recupererAffectationsExistantes();
        return ResponseEntity.ok(affectations);
    }
    
    
    //Méthode qui gère la requête GET pour récupérer les places disponibles par bureau.
    @GetMapping("/placesDisponibles")
    public ResponseEntity<Map<LocalDate, Map<String, Integer>>> getPlacesDisponibles() {
        Map<LocalDate, Map<String, Integer>> places=affectationService.recupererPlacesRestantesParBureau();
        return ResponseEntity.ok(places);
    }
    // Méthode qui gère la requête GET pour récupérer les affectations par équipe spécifiée.
    @GetMapping("/affectations/{nomEquipe}")
    public Map<LocalDate, List<String>> getAffectationsParEquipe(@PathVariable String nomEquipe) {
        return affectationService.getAffectationsParEquipe(nomEquipe);
    }
    //Méthode qui gère la requête POST pour affecter les bureaux aux équipes.
    @PostMapping("/affecterBureaux")
    public ResponseEntity<?> affecterBureaux() {
        Map<LocalDate, Map<String, Object>> affectations = affectationService.planifierAffectationHebdomadaire();
        return ResponseEntity.ok(affectations);
    }
    
}
