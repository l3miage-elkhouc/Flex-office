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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

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

    public Map<LocalDate, Map<String, String>> recupererAffectationsExistantes() {
        // Convertir Iterable en List
        List<Affectation> affectations = StreamSupport.stream(affectationRepository.findAll().spliterator(), false)
                                                    .collect(Collectors.toList());
        
        Map<LocalDate, Map<String, String>> resultat = affectations.stream()
            .collect(Collectors.groupingBy(Affectation::getDate,
                    Collectors.toMap(
                        affectation -> affectation.getEquipe().getNom(),
                        affectation -> affectation.getBureau().getNom(),
                        (bureau1, bureau2) -> bureau1
                    )));

        return resultat;
    }

    

    public Map<LocalDate, String> getAffectationsParEquipe(String nomEquipe) {
        List<Affectation> affectations = affectationRepository.findByNomEquipe(nomEquipe);
        Map<LocalDate, String> affectationsEquipe = new HashMap<>();
        
        for (Affectation affectation : affectations) {
            affectationsEquipe.put(affectation.getDate(), affectation.getBureau().getNom());
        }   
        return affectationsEquipe;
    }


@Transactional
public void sauvegarderAffectations(Map<LocalDate, Map<String, List<String>>> affectations) {
    affectations.forEach((jour, affectationsJour) -> {
        affectationsJour.forEach((nomEquipe, nomsBureaux) -> {
            Optional<Equipe> equipeOpt = equipeService.trouverParNom(nomEquipe);

            if (equipeOpt.isPresent()) {
                Equipe equipe = equipeOpt.get();
                nomsBureaux.forEach(nomBureau -> {
                    Optional<Bureau> bureauOpt = bureauService.trouverParNom(nomBureau);
                    if (bureauOpt.isPresent()) {
                        Bureau bureau = bureauOpt.get();
                        Affectation affectation = new Affectation();
                        affectation.setDate(jour);
                        affectation.setEquipe(equipe);
                        affectation.setBureau(bureau);
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



    // @Scheduled(cron = "0 30 12 * * SUN")
    public Map<LocalDate, Map<String, List<String>>> planifierAffectationHebdomadaire() {
        //Supprimer les anciennes affectations
        affectationRepository.deleteAll();
        Map<LocalDate, Map<String, List<String>>> affectations = affecterBureaux();
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

    public Map<LocalDate, Map<String, List<String>>> affecterBureaux() {
        // La structure des affectations est mise à jour pour permettre à chaque équipe d'être associée à plusieurs bureaux chaque jour.
        Map<LocalDate, Map<String, List<String>>> affectations = new LinkedHashMap<>();
    
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(4);
    
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {

            System.out.println(date);

            affectations.put(date, new LinkedHashMap<>());
            // Liste de tous les bureaux disponibles.
            List<Bureau> bureauxDisponibles = new ArrayList<>(bureauService.getBureaux());
            for (Equipe equipe : equipeService.getEquipes()) {
                //NB: S'assurer que la liste des jours de présence est initialisée pour éviter les NullPointerExceptions
                if (equipe.getJoursDePresence() == null || !equipe.getJoursDePresence().contains(date.getDayOfWeek().getValue())) {
                    continue; // Passer à la prochaine équipe si aujourd'hui n'est pas un jour de présence pour l'équipe
                }
                List<String> bureauxAttribues = new ArrayList<>();
                // Calculez le nombre de postes nécessaires pour l'équipe donc le nombre de personne par l'équipe.
                int membresRestants = equipe.getNombrePersonnes();

                while (membresRestants > 0 && !bureauxDisponibles.isEmpty()) {
                    Bureau bureauChoisi = choisirBureauPourEquipe(membresRestants, bureauxDisponibles);
                    if (bureauChoisi != null) {
                        bureauxAttribues.add(bureauChoisi.getNom()); // Ajoute le bureau attribué à la liste.
                        int nouvelleCapacite = bureauChoisi.getCapacite() - membresRestants;
                        membresRestants -= bureauChoisi.getCapacite(); // Réduit le nombre de postes restants à pourvoir.
                        
                        if(nouvelleCapacite<=0){
                            bureauxDisponibles.remove(bureauChoisi); // Retire le bureau choisi des bureaux disponibles.
                        }
                    } else {
                        //A faire Gestion du cas où il n'est pas possible d'attribuer suffisamment de bureaux.
                        break;
                    }
                }
    
                if (!bureauxAttribues.isEmpty()) {
                    affectations.get(date).put(equipe.getNom(), bureauxAttribues);
                } else {
                    System.out.println("Aucun bureau disponible pour l'équipe " + equipe.getNom() + " le " + date);
                }
            }
        }
    
        return affectations;
    }
    
    private Bureau choisirBureauPourEquipe(int membresRestants, List<Bureau> bureauxDisponibles) {
        // Cette méthode choisit un bureau adapté à la taille de l'équipe restante à placer. 
        for (Bureau bureau : bureauxDisponibles) {
            if (bureau.getCapacite() >= membresRestants) {
                return bureau;
            }
        }
        // Retourne le plus grand bureau disponible si aucun n'est assez grand pour toute l'équipe.
        return bureauxDisponibles.isEmpty() ? null : Collections.max(bureauxDisponibles, Comparator.comparingInt(Bureau::getCapacite));
    }
    
    public Map<Bureau, Integer> calculerPlacesDisponibles() {
        List<Bureau> bureaux = bureauService.getBureaux();
        // Cela suppose l'existence d'une méthode getAffectationsActives() renvoyant toutes les affectations actuellement valides
        List<Affectation> affectations = recupererToutesAffectations();
    
        // Préparation d'une map pour compter le nombre total de places occupées par bureau
        Map<Bureau, Integer> placesOccupéesParBureau = new HashMap<>();
    
        for (Affectation affectation : affectations) {
            Bureau bureau = affectation.getBureau();
            int nombrePersonnes = affectation.getEquipe().getNombrePersonnes(); // Cela suppose que chaque affectation a une équipe avec un nombre défini de personnes
    
            // Ajout ou mise à jour du nombre de personnes par bureau dans la map
            placesOccupéesParBureau.merge(bureau, nombrePersonnes, Integer::sum);
        }
    
        // Calcul des places disponibles par bureau
        Map<Bureau, Integer> placesDisponiblesParBureau = new HashMap<>();
        for (Bureau bureau : bureaux) {
            int placesOccupées = placesOccupéesParBureau.getOrDefault(bureau, 0);
            int placesDisponibles = Math.max(0, bureau.getCapacite() - placesOccupées); // S'assurer que le nombre de places disponibles ne soit jamais négatif
            placesDisponiblesParBureau.put(bureau, placesDisponibles);
        }
        return placesDisponiblesParBureau;
    }
}

