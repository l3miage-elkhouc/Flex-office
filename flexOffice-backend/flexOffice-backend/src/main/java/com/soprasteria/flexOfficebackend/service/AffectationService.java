package com.soprasteria.flexOfficebackend.service;

import com.soprasteria.flexOfficebackend.model.Bureau;
import com.soprasteria.flexOfficebackend.model.Equipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AffectationService {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private BureauService bureauService;

    // Méthode pour effectuer l'affectation des bureaux aux équipes pour chaque jour de la semaine
    public Map<String, Map<String, String>> affecterBureaux() {
        List<Equipe> equipes = equipeService.getEquipes();
        List<Bureau> bureaux = bureauService.getBureaux();

        Map<String, Map<String, String>> affectations = new HashMap<>();
        Map<String, String> affectationsJour;

        // Pour chaque jour de la semaine
        String[] joursSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        for (String jour : joursSemaine) {
            affectationsJour = new HashMap<>();

            // Liste des bureaux disponibles pour ce jour
            List<String> nomsBureaux = new ArrayList<>();
            for (Bureau bureau : bureaux) {
                nomsBureaux.add(bureau.getNom());
            }

            // Pour chaque équipe
            for (Equipe equipe : equipes) {
                // Trouver un bureau disponible pour cette équipe
                String bureauAttribue = trouverBureauDisponible(nomsBureaux);
                // Si aucun bureau n'est disponible pour cette équipe, on sort de la boucle
                if (bureauAttribue == null) {
                    System.out.println("Pas assez de bureaux disponibles pour toutes les équipes.");
                    return affectations;
                }
                // Enregistrer l'affectation pour cette équipe ce jour-là
                affectationsJour.put(equipe.getNom(), bureauAttribue);
                // Retirer le bureau attribué de la liste des bureaux disponibles pour les autres équipes
                nomsBureaux.remove(bureauAttribue);
            }

            // Ajouter les affectations pour ce jour à la liste globale
            affectations.put(jour, affectationsJour);
        }

        return affectations;
    }

    // Méthode pour trouver un bureau disponible pour une équipe
    private static String trouverBureauDisponible(List<String> nomsBureaux) {
        // Vérifie que la liste des bureaux disponibles n'est pas vide
        if (!nomsBureaux.isEmpty()) {
            // Choix aléatoire d'un bureau parmi la liste des bureaux disponibles
            Random rand = new Random();
            return nomsBureaux.get(rand.nextInt(nomsBureaux.size()));
        }
        return null; // Aucun bureau disponible
    }
}
