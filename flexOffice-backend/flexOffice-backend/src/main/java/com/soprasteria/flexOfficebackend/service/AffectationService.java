// package com.soprasteria.flexOfficebackend.service;
// import com.soprasteria.flexOfficebackend.model.Affectation;
// import com.soprasteria.flexOfficebackend.model.Bureau;
// import com.soprasteria.flexOfficebackend.model.Equipe;
// import com.soprasteria.flexOfficebackend.repository.AffectationRepository;

// import jakarta.transaction.Transactional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.scheduling.annotation.Scheduled;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.Random;

// @Service
// public class AffectationService {

//     private final AffectationRepository affectationRepository;

//     @Autowired
//     private EquipeService equipeService;

//     @Autowired
//     private BureauService bureauService;

//     @Autowired
//     public AffectationService(AffectationRepository affectationRepository) {
//         this.affectationRepository = affectationRepository;
//     }

//     // Ajouter une nouvelle affectation
//     public Affectation ajouterAffectation(Affectation affectation) {
//         return affectationRepository.save(affectation);
//     }

//     // Récupérer toutes les affectations
//     public List<Affectation> recupererToutesAffectations() {
//         List<Affectation> affectations = new ArrayList<>();
//         affectationRepository.findAll().forEach(affectations::add);
//         return affectations;
//     }

//     // Récupérer une affectation spécifique par son ID
//     public Optional<Affectation> recupererAffectationParId(Long id) {
//         return affectationRepository.findById(id);
//     }

//     // Mettre à jour une affectation (supposant que l'ID est inclus dans l'objet Affectation)
//     public Affectation mettreAJourAffectation(Affectation affectation) {
//         return affectationRepository.save(affectation); // save fonctionne pour à la fois créer et mettre à jour
//     }

//     // Supprimer une affectation par son ID
//     public void supprimerAffectation(Long id) {
//         affectationRepository.deleteById(id);
//     }

//     @Transactional
//     public void sauvegarderAffectations(Map<String, Map<String, String>> affectations) {
//         affectations.forEach((jour, affectationsJour) -> {
//             affectationsJour.forEach((idEquipe, idBureau) -> {
//                 Affectation affectation = new Affectation();
//                 affectation.setJour(jour);
//                 affectation.setIdEquipe(idEquipe);
//                 affectation.setIdBureau(idBureau);
//                 affectationRepository.save(affectation);
//             });
//         });
//     }


//     public Map<String, Map<String, String>> affecterBureaux() {
//         List<Equipe> equipes = equipeService.getEquipes();
//         // Trier les équipes par nombre de personnes décroissant
//         Collections.sort(equipes, Comparator.comparingInt(Equipe::getNombrePersonnes).reversed());

//         List<Bureau> bureaux = bureauService.getBureaux();
//         Map<String, Map<String, String>> affectations = new LinkedHashMap<>();

//         String[] joursSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
//         for (String jour : joursSemaine) {
//             Map<String, String> affectationsJour = new LinkedHashMap<>();
//             List<Bureau> bureauxDisponibles = new ArrayList<>(bureaux); // Copie de la liste des bureaux

//             for (Equipe equipe : equipes) {
//                 Bureau bureauAttribue = trouverBureauDisponible(equipe.getNombrePersonnes(), bureauxDisponibles);
//                 if (bureauAttribue != null) {
//                     affectationsJour.put(equipe.getNom(), bureauAttribue.getNom());
//                     bureauxDisponibles.remove(bureauAttribue); // Enlever le bureau attribué de la liste des disponibles
//                 } else {
//                     System.out.println("Pas assez de bureaux disponibles pour toutes les équipes ou capacité insuffisante.");
//                     break; // Sortir si aucun bureau disponible ou si la capacité n'est pas suffisante
//                 }
//             }

//             affectations.put(jour, affectationsJour);
//         }

//         return affectations;
//     }

//         private static Bureau trouverBureauDisponible(int nombrePersonnes, List<Bureau> nomsBureaux) {
//             // Vérifie que la liste des bureaux disponibles n'est pas vide
//             for (Bureau bureau : nomsBureaux) {

//             if (!nomsBureaux.isEmpty() && (bureau.getCapacite() >= nombrePersonnes) ) {
//                 // Choix aléatoire d'un bureau parmi la liste des bureaux disponibles
//                 Random rand = new Random();
//                 return nomsBureaux.get(rand.nextInt(nomsBureaux.size()));
//             }
//             }
//             return null; // Aucun bureau disponible
//         }

//         public Map<String, String> getAffectationsParEquipe(String nomEquipe) {
//         Map<String, Map<String, String>> affectationsGlobales = this.affecterBureaux();
//         Map<String, String> affectationsEquipe = new HashMap<>();
    
//         affectationsGlobales.forEach((jour, affectationsJour) -> {
//             if(affectationsJour.containsKey(nomEquipe)) {
//                 affectationsEquipe.put(jour, affectationsJour.get(nomEquipe));
//             }
//         });
    
//         return affectationsEquipe;
//     }

    
//     @Scheduled(cron = "0 0 0 * * THU")
//     public void planifierAffectationHebdomadaire() {
//         Map<String, Map<String, String>> affectations = affecterBureaux();
//         sauvegarderAffectations(affectations);
//     }
// }

package com.soprasteria.flexOfficebackend.service;
import com.soprasteria.flexOfficebackend.model.Affectation;
import com.soprasteria.flexOfficebackend.model.Bureau;
import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.repository.AffectationRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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

    @Transactional
    public void sauvegarderAffectations(Map<String, Map<String, String>> affectations) {
        affectations.forEach((jour, affectationsJour) -> {
            affectationsJour.forEach((equipe, bureau) -> {
                Optional<Equipe> equipeOpt = equipeService.trouverParNom(equipe);
                Optional<Bureau> bureauOpt = bureauService.trouverParNom(bureau);

                Affectation affectation = new Affectation();
                affectation.setJour(jour);
                affectation.setEquipe(equipeOpt.get());
                affectation.setBureau(bureauOpt.get());
                affectationRepository.save(affectation);
            });
        });
    }


    public Map<String, Map<String, String>> affecterBureaux() {
        List<Equipe> equipes = equipeService.getEquipes();
        // Trier les équipes par nombre de personnes décroissant
        Collections.sort(equipes, Comparator.comparingInt(Equipe::getNombrePersonnes).reversed());
    
        List<Bureau> bureaux = bureauService.getBureaux();
        Map<String, Map<String, String>> affectations = new LinkedHashMap<>();
    
        String[] joursSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        for (String jour : joursSemaine) {
            Map<String, String> affectationsJour = new LinkedHashMap<>();
            List<Bureau> bureauxDisponibles = new ArrayList<>(bureaux); // Copie de la liste des bureaux
    
            for (Equipe equipe : equipes) {
                Bureau bureauAttribue = trouverBureauDisponible(equipe.getNombrePersonnes(), bureauxDisponibles);
                if (bureauAttribue != null) {
                    affectationsJour.put(equipe.getNom(), bureauAttribue.getNom());
                    bureauxDisponibles.remove(bureauAttribue); // Enlever le bureau attribué de la liste des disponibles
                } else {
                    System.out.println("Pas assez de bureaux disponibles pour toutes les équipes ou capacité insuffisante.");
                    break; // Sortir si aucun bureau disponible ou si la capacité n'est pas suffisante
                }
            }
    
            affectations.put(jour, affectationsJour);
        }
    
        return affectations;
    }
    

        private static Bureau trouverBureauDisponible(int nombrePersonnes, List<Bureau> nomsBureaux) {
            // Vérifie que la liste des bureaux disponibles n'est pas vide
            for (Bureau bureau : nomsBureaux) {

            if (!nomsBureaux.isEmpty() && (bureau.getCapacite() >= nombrePersonnes) ) {
                // Choix aléatoire d'un bureau parmi la liste des bureaux disponibles
                Random rand = new Random();
                return nomsBureaux.get(rand.nextInt(nomsBureaux.size()));
            }
            }
            return null; // Aucun bureau disponible
        }

        
        // public Map<String, String> getAffectationsParEquipe(String nomEquipe) {
        // Map<String, Map<String, String>> affectationsGlobales = this.affecterBureaux();
        // Map<String, String> affectationsEquipe = new HashMap<>();
    
        // affectationsGlobales.forEach((jour, affectationsJour) -> {
        //     if(affectationsJour.containsKey(nomEquipe)) {
        //         affectationsEquipe.put(jour, affectationsJour.get(nomEquipe));
        //     }
        // });
    
        // return affectationsEquipe;
        // }
        
    @Scheduled(cron = "0 40 11 * * THU")
    public void planifierAffectationHebdomadaire() {
		System.out.println("ici");

        //Supprimer les anciennes affectations
        affectationRepository.deleteAll();
        Map<String, Map<String, String>> affectations = affecterBureaux();
        sauvegarderAffectations(affectations);
    }



    public Map<String, Map<String, String>> recupererAffectationsExistantes() {
        // Convertir Iterable en List
        List<Affectation> affectations = StreamSupport.stream(affectationRepository.findAll().spliterator(), false)
                                                    .collect(Collectors.toList());
        
        Map<String, Map<String, String>> resultat = affectations.stream()
            .collect(Collectors.groupingBy(Affectation::getJour,
                    Collectors.toMap(
                        affectation -> affectation.getEquipe().getNom(),
                        affectation -> affectation.getBureau().getNom(),
                        (bureau1, bureau2) -> bureau1
                    )));

        return resultat;
    }

    public Map<String, String> getAffectationsParEquipe(String nomEquipe) {
        List<Affectation> affectations = affectationRepository.findByNomEquipe(nomEquipe);
        Map<String, String> affectationsEquipe = new HashMap<>();
        
        for (Affectation affectation : affectations) {
            affectationsEquipe.put(affectation.getJour(), affectation.getBureau().getNom());
        }
        
        return affectationsEquipe;
    }
}

