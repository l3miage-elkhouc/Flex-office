package com.soprasteria.flexOfficebackend.service;

import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.repository.EquipeRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    public List<Equipe> getEquipes() {
        List<Equipe> equipes = new ArrayList<>();
        equipeRepository.findAll().forEach(equipe -> {
            equipes.add(equipe);
        });
        return equipes;
    }

    public Equipe getEquipe(int id) {
        return equipeRepository.findById(id).orElse(null);
    }

    public void addEquipe(Equipe equipe) {
        equipeRepository.save(equipe);
    }

    public void updateEquipe(Equipe equipeUpdates,int equipeId) {
        Equipe equipeExistante = equipeRepository.findById(equipeId).orElseThrow(() -> new RuntimeException("Equipe non trouvée"));
            equipeExistante.setNombrePersonnes(equipeUpdates.getNombrePersonnes());
        equipeRepository.save(equipeExistante);
    }
    

    public Optional<Equipe> trouverParNom(String nom) {
        return equipeRepository.findByNom(nom); // Supposant que cette méthode est définie dans EquipeRepository
    }

    public void updateJoursDePresence(int equipeId, List<Integer> joursDePresence) {
        // Recherche l'équipe par son ID. Lance une exception si non trouvée.
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Equipe non trouvée avec l'ID : " + equipeId));
        
        // Met à jour les jours de présence de l'équipe.
        equipe.setJoursDePresence(joursDePresence);

        // Sauvegarde l'équipe mise à jour dans la base de données.
        equipeRepository.save(equipe);
    
}
    // EquipeService.java
    public void deleteEquipe(int id) {
        equipeRepository.deleteById(id);
    }

    
}
