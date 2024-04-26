import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AffectationService } from '../../services/affectation.service';
import { EquipeService } from '../../services/equipe.service';
import { AffectationsSemaine, PlacesDisponibles } from '../../models/affectation.model';
import { BureauService } from '../../services/bureau.service';

@Component({
  selector: 'app-visualisation',
  templateUrl: './visualisation.component.html',
  styleUrl: './visualisation.component.css'
})
export class VisualisationComponent implements OnInit {
  userName: string = '';
  equipeName: string = '';
  dataLoaded: boolean = false;
  admin!: boolean;
  affectationsSemaine: AffectationsSemaine | undefined;
  transformedAffectations: any[] = [];
  jours: string[] = [];
  equipes: any[] = [];
  bureaux: any[] = [];
  remainingPlaces: { bureau: string, placesRestantes: number, jour: string }[] = [];
  placesDisponibles: any; 
  
  constructor(private authService: AuthService,private affectationService: AffectationService, private equipeService: EquipeService, private bureauService: BureauService) { }

  ngOnInit() {
    this.authService.getUserData().subscribe(
      ([userName, equipeName, admin]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.admin=admin;
                
      }
    )

      // Récupération des places disponibles depuis le service d'affectation
      this.affectationService.getPlacesDisponibles().subscribe(data => {

        console.log("places restantes :", data);
        this.placesDisponibles = data;
      });
    
    // Récupération des affectations depuis le service d'affectation
    this.affectationService.getAffectations().subscribe(data => {
      console.log("Données d'affectations reçues du backend :", data);
      this.affectationsSemaine = data;
      this.transformData(data);
      this.updateRemainingPlaces(); // Appel de la méthode pour mettre à jour les places restantes
    });
  
    // Récupération des équipes depuis le service d'équipe
    this.equipeService.getEquipes().subscribe(data => {
      this.equipes = data;
      this.updateRemainingPlaces(); // Appel de la méthode pour mettre à jour les places restantes après récupération des équipes
    });

    // Récupération des bureaux depuis le service de bureau
    this.bureauService.getBureaux().subscribe(data => {
      this.bureaux = data;
    });
  }


    /**
   * Transforme les données d'affectations pour chaque semaine en une structure facilement utilisable par l'application.
   * Cette fonction traite les données brutes d'affectation, organisant les informations par équipe et par jour,
   * et prépare une structure de données qui indique quel bureau (ou quels bureaux) chaque équipe occupe chaque jour de la semaine.
   *
   * Les données d'entrée doivent avoir une forme où chaque clé représente une date sous forme de chaîne ISO,
   * et la valeur est un objet dont les clés sont les noms des équipes et les valeurs sont les bureaux (un seul bureau ou un tableau de bureaux)
   * assignés à ces équipes pour la date correspondante.
   *
   * La fonction crée une nouvelle structure de données où chaque élément représente une équipe et ses affectations de bureau pour chaque jour.
   * Pour les jours où une équipe est assignée à plusieurs bureaux, les noms de ces bureaux sont regroupés dans un tableau.
   * Si une équipe n'a pas d'affectation pour un jour donné, la valeur est une chaîne vide.
   *
   * @param {AffectationsSemaine} data - Les données d'affectation brutes pour une semaine donnée.
   */
  transformData(data: AffectationsSemaine) {
    console.log(this.affectationsSemaine);
    // Initialisation des jours avec les noms des jours de la semaine
    this.jours = Object.keys(data).map(dateIso => this.getDayName(dateIso));
  
    // Initialise la structure transformée
    this.transformedAffectations = [];
  
    const datesIso = Object.keys(data); // Les clés sont les dates ISO
  
    // Crée un ensemble pour stocker de manière unique toutes les équipes
    const teams = new Set<string>();
  
    // Parcourt chaque date pour remplir l'ensemble des équipes
    datesIso.forEach(dateIso => {
      const affectationsDuJour = data[dateIso];
      Object.keys(affectationsDuJour).forEach(equipe => {
        teams.add(equipe); // Ajoute chaque équipe rencontrée à l'ensemble
      });
    });
  
    // Crée la structure de données pour chaque équipe
    teams.forEach(equipe => {
      let affectationObj: any = { equipe: equipe };
      datesIso.forEach(dateIso => {
        const jourNom = this.getDayName(dateIso); // Convertit en nom de jour
        const bureauxDuJour = data[dateIso][equipe];
        if (Array.isArray(bureauxDuJour)) {
          affectationObj[jourNom] = Array.isArray(bureauxDuJour) ? bureauxDuJour : [bureauxDuJour || ''];

        } else {
          // Sinon, utilisez la valeur telle quelle (peut être une chaîne ou undefined)
          affectationObj[jourNom] = bureauxDuJour || ''; // S'il n'y a pas d'affectation, on laisse une chaîne vide
        }
      });
      this.transformedAffectations.push(affectationObj);
    });
  
    console.log(this.transformedAffectations);
  }
  
  
  // Définition de la méthode qui utilise calculPlacesRestantes

      getDayName(dateIsoString: string): string {
        const date = new Date(dateIsoString);
        return new Intl.DateTimeFormat('fr-FR', { weekday: 'long' }).format(date);
      }

      // Calcul des places restantes pour chaque bureau
      calculPlacesRestantes(placesDisponibles: any): { bureau: string, placesRestantes: number, jour: string }[] {
        const result: { bureau: string, placesRestantes: number, jour: string }[] = [];
        
        // Parcourir chaque date dans l'objet reçu
        Object.entries(placesDisponibles).forEach(([date, bureauxInfo]) => {
          
            // Parcourir chaque bureau dans l'objet pour la date donnée
            Object.entries(bureauxInfo as { [key: string]: number }).forEach(([bureau, placesRestantes]) => {
                result.push({
                    bureau: bureau,
                    placesRestantes: placesRestantes,
                    jour: this.getDayName(date),
                });
            });
        });

        return result;
    }
   // Mise à jour des places restantes
    updateRemainingPlaces() {
      if (this.placesDisponibles) {
        this.remainingPlaces = this.calculPlacesRestantes(this.placesDisponibles);
      }
    }

   // Méthode pour obtenir la capacité d'un bureau spécifique
    getBureauCapacite(nomBureau: string): number | undefined {
      const bureau = this.bureaux.find(b => b.nom === nomBureau);
      return bureau ? bureau.capacite : undefined;
    }
  
    // Méthode pour obtenir le nombre de personnes dans une équipe spécifique
    getNombrePersonnes(nomEquipe: string): number | undefined {
      const equipe = this.equipes.find(e => e.nom === nomEquipe);
      return equipe ? equipe.nombrePersonnes : undefined;
    }

    // Méthode pour obtenir les places restantes pour un bureau donné à une date spécifique
    getPlacesRestantes(bureau: string, jour: string): number | undefined {
      const place = this.remainingPlaces.find(place => place.bureau === bureau && place.jour === jour);
      return place ? place.placesRestantes : undefined;
    }

  
    // Gestion du clic sur le bouton d'affectation des bureaux
    onAffecterBureauxClick() {
      this.affectationService.affecterBureaux().subscribe(result => {
        window.location.reload();
        console.log('Affectations réalisées avec succès', result);
      }, error => {
        console.error('Erreur lors de l\'affectation des bureaux', error);
      });
    }       
}
