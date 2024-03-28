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

    public void deleteEquipe(int id) {
        equipeRepository.deleteById(id);
    }

    public void addEquipe(Equipe equipe) {
        equipeRepository.save(equipe);
    }

    public void updateEquipe(Equipe equipe, int id) {
        equipe.setId(id); // Assurez-vous que l'ID est défini pour la mise à jour
        equipeRepository.save(equipe);
    }

    public Optional<Equipe> trouverParNom(String nom) {
        return equipeRepository.findByNom(nom); // Supposant que cette méthode est définie dans EquipeRepository
    }
    
}
