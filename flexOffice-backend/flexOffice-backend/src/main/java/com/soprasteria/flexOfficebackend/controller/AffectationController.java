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



@CrossOrigin(origins="*")
@RestController
@RequestMapping("/affectations")
public class AffectationController {

    @Autowired
    private AffectationService affectationService;
    
    @GetMapping
    public ResponseEntity<Map<LocalDate, Map<String, String>>> getAffectations() {
        Map<LocalDate, Map<String, String>> affectations = affectationService.recupererAffectationsExistantes();
        return ResponseEntity.ok(affectations);
    }

    @GetMapping("/affectations/{nomEquipe}")
    public Map<LocalDate, String> getAffectationsParEquipe(@PathVariable String nomEquipe) {
        return affectationService.getAffectationsParEquipe(nomEquipe);
    }

    @PostMapping("/affecterBureaux")
    public ResponseEntity<?> affecterBureaux() {
        Map<LocalDate, Map<String, List<String>>> affectations = affectationService.planifierAffectationHebdomadaire();
        // Logique pour sauvegarder ou traiter les affectations...
        return ResponseEntity.ok(affectations);
    }
    
}
