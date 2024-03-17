package com.soprasteria.flexOfficebackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soprasteria.flexOfficebackend.model.Utilisateur;
import com.soprasteria.flexOfficebackend.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getUtilisateurs(){
        List<Utilisateur> utilisateurs = new ArrayList<>();
        utilisateurRepository.findAll().forEach(utilisateur -> {
            utilisateurs.add(utilisateur);
        });
        return utilisateurs;
    }
    public Utilisateur getUtilisateur(int id){
        return utilisateurRepository.findById(id).orElse(null);
    }
    public void deleteUtilisateur(int id){
        utilisateurRepository.deleteById(id);
    }

    public void addUtilisateur(Utilisateur utilisateur){
        utilisateurRepository.save(utilisateur);
    }
    public void updateUtilisateur(Utilisateur utilisateur, int id){
        utilisateurRepository.save(utilisateur);
    }
    public boolean checkEmailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
}
