package com.soprasteria.flexOfficebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.repository.EquipeRepository;
import com.soprasteria.flexOfficebackend.service.EquipeService;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @RequestMapping("/equipes")
    public List<Equipe> getEquipes() {
        return equipeService.getEquipes();
    }

    @RequestMapping("/equipe/{id}")
    public Equipe getEquipe(@PathVariable int id) {
        return equipeService.getEquipe(id);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/equipes")
    public void addEquipe(@RequestBody Equipe equipe) {
        equipeService.addEquipe(equipe);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/equipes/{id}")
    public void updateEquipe(@RequestBody Equipe equipe, @PathVariable int id) {
        equipeService.updateEquipe(equipe, id);
    }

    @Autowired
    private EquipeRepository equipeRepository;
    @PostMapping("/addEquipes")
    public ResponseEntity<Equipe> ajouterEquipe(@RequestBody Equipe equipe) {
        @SuppressWarnings("null")
        Equipe nouvelleEquipe = equipeRepository.save(equipe);
        return ResponseEntity.ok(nouvelleEquipe);
    }

    @PutMapping("/equipes/{equipeId}/joursDePresence")
    public ResponseEntity<?> updateJoursDePresence(@PathVariable int equipeId, @RequestBody List<Integer> joursDePresence) {
        try {
            // Appel au service pour mettre à jour les jours de présence
            equipeService.updateJoursDePresence(equipeId, joursDePresence);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Gestion des erreurs, par exemple, si l'équipe n'existe pas
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EquipeController.java
    @DeleteMapping("/equipes/{id}")
    public ResponseEntity<?> deleteEquipe(@PathVariable int id) {
        equipeService.deleteEquipe(id);
        return ResponseEntity.ok().build();
    }

    
}
