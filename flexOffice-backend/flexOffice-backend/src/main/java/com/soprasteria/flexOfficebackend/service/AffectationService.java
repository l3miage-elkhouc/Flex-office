package com.soprasteria.flexOfficebackend.service;
import com.soprasteria.flexOfficebackend.model.Affectation;
import com.soprasteria.flexOfficebackend.model.Bureau;
import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.repository.AffectationRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AffectationService {

    private final AffectationRepository affectationRepository;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private BureauService bureauService;

    @Autowired
    public AffectationService(AffectationRepository affectationRepository) {
        this.affectationRepository = affectationRepository;
    }

    // Ajouter une nouvelle affectation
    public Affectation ajouterAffectation(Affectation affectation) {
        return affectationRepository.save(affectation);
    }

    // Récupérer toutes les affectations
    public List<Affectation> recupererToutesAffectations() {
        List<Affectation> affectations = new ArrayList<>();
        affectationRepository.findAll().forEach(affectations::add);
        return affectations;
    }

    // Récupérer une affectation spécifique par son ID
    public Optional<Affectation> recupererAffectationParId(Long id) {
        return affectationRepository.findById(id);
    }

    // Mettre à jour une affectation (supposant que l'ID est inclus dans l'objet Affectation)
    public Affectation mettreAJourAffectation(Affectation affectation) {
        return affectationRepository.save(affectation); // save fonctionne pour à la fois créer et mettre à jour
    }

    // Supprimer une affectation par son ID
    public void supprimerAffectation(Long id) {
        affectationRepository.deleteById(id);
    }

    public Map<LocalDate, Map<String, List<String>>> recupererAffectationsExistantes() {
        // Convertir Iterable en List
        List<Affectation> affectations = StreamSupport.stream(affectationRepository.findAll().spliterator(), false)
                                                        .collect(Collectors.toList());
    
        // On ajuste ici pour créer une Map<LocalDate, Map<String, List<String>>>
        Map<LocalDate, Map<String, List<String>>> resultat = affectations.stream()
            .collect(Collectors.groupingBy(Affectation::getDate,
                    Collectors.groupingBy(affectation -> affectation.getEquipe().getNom(),
                            Collectors.mapping(affectation -> affectation.getBureau().getNom(), Collectors.toList())
                    )));
    
        return resultat;
    }

    public Map<LocalDate, Map<String, Integer>> recupererPlacesRestantesParBureau() {
        // Récupérer toutes les affectations
        List<Affectation> affectations = StreamSupport.stream(affectationRepository.findAll().spliterator(), false)
                                                       .collect(Collectors.toList());
    
        // Structure pour stocker les résultats
        Map<LocalDate, Map<String, Integer>> resultat = affectations.stream()
            .collect(Collectors.groupingBy(
                Affectation::getDate,
                Collectors.toMap(
                    affectation -> affectation.getBureau().getNom(),
                    Affectation::getPlacesRestantes,
                    Integer::sum // Si plusieurs affectations pour le même bureau le même jour, additionnez les places restantes
                )
            ));
    
        return resultat;
    }
    
    

    
public Map<LocalDate, List<String>> getAffectationsParEquipe(String nomEquipe) {
    List<Affectation> affectations = affectationRepository.findByNomEquipe(nomEquipe);
    Map<LocalDate, List<String>> affectationsEquipe = new HashMap<>();

    for (Affectation affectation : affectations) {
        LocalDate date = affectation.getDate();
        String bureauNom = affectation.getBureau().getNom();

        // Vérifie si la date existe déjà dans la map
        if (!affectationsEquipe.containsKey(date)) {
            // Si non, crée une nouvelle liste pour cette date
            affectationsEquipe.put(date, new ArrayList<>());
        }
        // Ajoute le bureau à la liste existante pour cette date
        affectationsEquipe.get(date).add(bureauNom);
    }   
    return affectationsEquipe;
}



@Transactional
    public void sauvegarderAffectations(Map<LocalDate, Map<String, Object>> affectations) {
        affectations.forEach((jour, dailyData) -> {
            Map<String, List<String>> bureauAffectations = (Map<String, List<String>>) dailyData.get("affectations");
            Map<String, Integer> capacitesRestantes = (Map<String, Integer>) dailyData.get("capacitesRestantes");
    
            bureauAffectations.forEach((nomEquipe, nomsBureaux) -> {
                Optional<Equipe> equipeOpt = equipeService.trouverParNom(nomEquipe);
    
                if (equipeOpt.isPresent()) {
                    Equipe equipe = equipeOpt.get();
                    nomsBureaux.forEach(nomBureau -> {
                        Optional<Bureau> bureauOpt = bureauService.trouverParNom(nomBureau);
                        if (bureauOpt.isPresent()) {
                            Bureau bureau = bureauOpt.get();
                            int placesRestantes = capacitesRestantes.getOrDefault(nomBureau, 0);
                            Affectation affectation = new Affectation();
                            affectation.setDate(jour);
                            affectation.setEquipe(equipe);
                            affectation.setBureau(bureau);
                            affectation.setPlacesRestantes(placesRestantes); // Set places restantes ici
                            affectationRepository.save(affectation);
                        } else {
                            System.out.println("Bureau introuvable avec le nom : " + nomBureau);
                        }
                    });
                } else {
                    System.out.println("Équipe introuvable avec le nom : " + nomEquipe);
                }
            });
        });
    }
    



    public Map<LocalDate, Map<String, Object>> planifierAffectationHebdomadaire() {
        //Supprimer les anciennes affectations
        affectationRepository.deleteAll();
        Map<LocalDate, Map<String, Object>>affectations = affecterBureaux();
        sauvegarderAffectations(affectations);
        return affectations;
    }

        
    private int calculerJoursDePresence(BigDecimal tauxPresence) {
    final BigDecimal joursDansLaSemaine = BigDecimal.valueOf(5);
    return tauxPresence.divide(BigDecimal.valueOf(100))
                        .multiply(joursDansLaSemaine)
                        .setScale(0, RoundingMode.HALF_UP)
                        .intValue();
    }

    public Map<LocalDate, Map<String, Object>> affecterBureaux() {
        Map<LocalDate, Map<String, Object>> affectations = new LinkedHashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(4);
    
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            System.out.println(date);
    
            // Initialisation des données par jour
            Map<String, Object> dailyData = new LinkedHashMap<>();
            List<Bureau> bureauxDisponibles = new ArrayList<>(bureauService.getBureaux());
            Map<Bureau, Integer> capacitesRestantes = bureauxDisponibles.stream()
                .collect(Collectors.toMap(bureau -> bureau, Bureau::getCapacite));
    
            Map<String, List<String>> bureauAffectations = new LinkedHashMap<>();
            
            for (Equipe equipe : equipeService.getEquipes()) {
                if (equipe.getJoursDePresence() == null || !equipe.getJoursDePresence().contains(date.getDayOfWeek().getValue())) {
                    continue;
                }
                List<String> bureauxAttribues = new ArrayList<>();
                int membresRestants = equipe.getNombrePersonnes();
    
                while (membresRestants > 0 && !bureauxDisponibles.isEmpty()) {
                    Bureau bureauChoisi = choisirBureauPourEquipe(membresRestants, bureauxDisponibles, capacitesRestantes);
                    if (bureauChoisi != null) {
                        int capaciteRestante = capacitesRestantes.get(bureauChoisi);
                        int placesUtilisees = Math.min(capaciteRestante, membresRestants);
                        membresRestants -= placesUtilisees;
                        capacitesRestantes.put(bureauChoisi, capaciteRestante - placesUtilisees);
    
                        bureauxAttribues.add(bureauChoisi.getNom());
                        if (capaciteRestante - placesUtilisees <= 0) {
                            bureauxDisponibles.remove(bureauChoisi);
                        }
                    }
                }
    
                if (!bureauxAttribues.isEmpty()) {
                    bureauAffectations.put(equipe.getNom(), bureauxAttribues);
                }
            }
    
            // Stocker les affectations et les capacités restantes
            dailyData.put("affectations", bureauAffectations);
            dailyData.put("capacitesRestantes", capacitesRestantes.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getNom(), Map.Entry::getValue)));
            affectations.put(date, dailyData);
        }
        return affectations;
    }
    
    private Bureau choisirBureauPourEquipe(int membresRestants, List<Bureau> bureauxDisponibles, Map<Bureau, Integer> capacitesRestantes) {
        // Filtrez d'abord les bureaux qui peuvent accueillir les membres restants basés sur la capacité restante.
        List<Bureau> bureauxCandidates = bureauxDisponibles.stream()
                .filter(bureau -> capacitesRestantes.get(bureau) >= membresRestants)
                .collect(Collectors.toList());
    
        if (!bureauxCandidates.isEmpty()) {
            // Choisissez le bureau avec la capacité restante la plus proche du nombre de membres restants,
            // pour optimiser l'utilisation de l'espace.
            return Collections.min(bureauxCandidates, Comparator.comparingInt(bureau -> capacitesRestantes.get(bureau) - membresRestants));
        } else {
            // Si aucun bureau ne peut accueillir tous les membres restants, choisissez le bureau avec la plus grande capacité restante.
            // Cela peut être nécessaire si vous décidez de permettre l'attribution de plusieurs bureaux à une équipe.
            return bureauxDisponibles.stream()
                    .max(Comparator.comparingInt(capacitesRestantes::get))
                    .orElse(null); // Retourne null si tous les bureaux sont pleins ou si la liste est vide.
        }
    }

}

