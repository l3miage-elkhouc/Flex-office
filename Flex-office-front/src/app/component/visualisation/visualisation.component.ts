import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';
import { AffectationService } from '../../services/affectation.service';
import { EquipeService } from '../../services/equipe.service';
import { AffectationsSemaine } from '../../models/affectation.model';
import { BureauService } from '../../services/bureau.service';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
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

  constructor(private authService: AuthService,private affectationService: AffectationService, private equipeService: EquipeService, private bureauService: BureauService) { }

  ngOnInit() {
    this.authService.getUserData().subscribe(
      ([userName, equipeName, admin]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.admin=admin;
        
      }
    )

    
    this.affectationService.getAffectations().subscribe(data => {

      console.log("Données d'affectations reçues du backend :", data);
      this.affectationsSemaine = data;
      this.transformData(data);
      this.updateRemainingPlaces(); // Appel de la méthode pour mettre à jour les places restantes
    });
  

    this.equipeService.getEquipes().subscribe(data => {
      this.equipes = data;
      this.updateRemainingPlaces(); // Appel de la méthode pour mettre à jour les places restantes après récupération des équipes
    });

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

    /**
   * Calcule les places restantes dans chaque bureau pour chaque jour basé sur les affectations fournies.
   * Prend en entrée un objet d'affectations où chaque clé est une date (sous forme de chaîne) et chaque valeur
   * est un objet représentant les affectations des équipes pour cette date, avec des équipes comme clés et des bureaux (ou un tableau de bureaux) comme valeurs.
   * Renvoie un tableau d'objets, chacun indiquant le bureau, le nombre de places restantes dans ce bureau, et le jour correspondant.
   *
   * @param {any} affectations - Un objet représentant les affectations d'équipe par jour.
   * @returns {Array} Un tableau d'objets avec les clés 'bureau', 'placesRestantes', et 'jour', représentant respectivement
   *                  le nom du bureau, le nombre de places restantes dans le bureau, et le nom du jour correspondant à l'affectation.
   */
  calculPlacesRestantes(affectations: any): { bureau: string, placesRestantes: number, jour: string }[] {
    const result: { bureau: string, placesRestantes: number, jour: string }[] = [];
    const capaciteParBureau: { [key: string]: number } = {}; // Pour stocker la capacité de chaque bureau
    const occupationParBureauEtJour: { [key: string]: { [key: string]: number } } = {}; // Pour stocker l'occupation de chaque bureau par jour
  
    // Pré-calcul de la capacité de chaque bureau
    this.bureaux.forEach(bureau => {
      capaciteParBureau[bureau.nom] = bureau.capacite;
      occupationParBureauEtJour[bureau.nom] = {};
    });
  
    Object.keys(affectations).forEach(date => {
      const jourNom = this.getDayName(date); // Assurez-vous que la conversion de date fonctionne correctement
      const equipesAffectations = affectations[date];
  
      Object.keys(equipesAffectations).forEach(equipe => {
        const equipeDetails = this.equipes.find(e => e.nom === equipe);
        if (!equipeDetails) return;
  
        const bureaux = Array.isArray(equipesAffectations[equipe]) ? equipesAffectations[equipe] : [equipesAffectations[equipe]];
        let nombrePersonnes = equipeDetails.nombrePersonnes;
  
        bureaux.forEach((bureau: string | number) => {
          if (!bureau) return;
          const capaciteBureau = capaciteParBureau[bureau];
          if (capaciteBureau === undefined) return;
  
          if (!occupationParBureauEtJour[bureau][jourNom]) {
            occupationParBureauEtJour[bureau][jourNom] = 0;
          }
  
          const placesOccupables = Math.min(nombrePersonnes, capaciteBureau - occupationParBureauEtJour[bureau][jourNom]);
          occupationParBureauEtJour[bureau][jourNom] += placesOccupables;
          nombrePersonnes -= placesOccupables; // Réduire le nombre de personnes pour les bureaux suivants
        });
      });
  
      // Après avoir calculé l'occupation totale pour chaque bureau, calculer les places restantes
      Object.keys(occupationParBureauEtJour).forEach(bureau => {
        const occupation = occupationParBureauEtJour[bureau][jourNom] || 0;
        const placesRestantes = capaciteParBureau[bureau] - occupation;
        result.push({
          bureau: bureau,
          placesRestantes: placesRestantes,
          jour: jourNom
        });
      });
    });
  
    return result;
  }
  

  updateRemainingPlaces() {
    if (this.affectationsSemaine) {
      this.remainingPlaces = this.calculPlacesRestantes(this.affectationsSemaine);
    }
  }

  getBureauCapacite(nomBureau: string): number | undefined {
    const bureau = this.bureaux.find(b => b.nom === nomBureau);
    return bureau ? bureau.capacite : undefined;
  }
  getNombrePersonnes(nomEquipe: string): number | undefined {
    const equipe = this.equipes.find(e => e.nom === nomEquipe);
    return equipe ? equipe.nombrePersonnes : undefined;
  }

  getPlacesRestantes(bureau: string, jour: string): number | undefined {
    const placeRestante = this.remainingPlaces.find(place => place.bureau === bureau && place.jour === jour);
    return placeRestante ? placeRestante.placesRestantes : undefined;
  }
  

    onAffecterBureauxClick() {
      this.affectationService.affecterBureaux().subscribe(result => {
        window.location.reload();
        console.log('Affectations réalisées avec succès', result);
      }, error => {
        console.error('Erreur lors de l\'affectation des bureaux', error);
      });
    }       
}
