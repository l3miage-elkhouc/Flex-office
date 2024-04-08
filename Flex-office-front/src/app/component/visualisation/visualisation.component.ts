import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';
import { AffectationService } from '../../services/affectation.service';
import { EquipeService } from '../../services/equipe.service';
import { AffectationsSemaine } from '../../models/affectation.model';
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



  constructor(private authService: AuthService,private affectationService: AffectationService, private equipeService: EquipeService, private bureauService: BureauService) { }

  ngOnInit() {
    this.authService.getUserData().subscribe(
      ([userName, equipeName, admin]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.admin=admin;}
    )
    this.affectationService.getAffectations().subscribe(data => {
      this.affectationsSemaine = data;
      this.transformData(data);
      this.updateRemainingPlaces(); // Appel de la méthode pour mettre à jour les places restantes
      // console.log(this.updateRemainingPlaces());
    });
  

    this.equipeService.getEquipes().subscribe(data => {
      this.equipes = data;
      this.updateRemainingPlaces(); // Appel de la méthode pour mettre à jour les places restantes après récupération des équipes
    });

    this.bureauService.getBureaux().subscribe(data => {
      this.bureaux = data;
    });
  }
  transformData(data: AffectationsSemaine) {
    // Initialisation de jours avec les noms de jours de la semaine
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
        teams.add(equipe);
      });
    });
  
    // Crée la structure de données pour chaque équipe
    teams.forEach(equipe => {
      let affectationObj: any = { equipe: equipe };
      datesIso.forEach(dateIso => {
        const jourNom = this.getDayName(dateIso); // Convertit en nom de jour
        affectationObj[jourNom] = data[dateIso][equipe] || '';
      });
      this.transformedAffectations.push(affectationObj);
    });
  }
  // Définition de la méthode qui utilise calculPlacesRestantes

  getDayName(dateIsoString: string): string {
    const date = new Date(dateIsoString);
    return new Intl.DateTimeFormat('fr-FR', { weekday: 'long' }).format(date);
  }

  // calculPlacesRestantes(affectations: any): { bureau: string, placesRestantes: number }[] {
  //   const result: { bureau: string, placesRestantes: number }[] = [];
  //   console.log(affectations);
  
  //   Object.keys(affectations).forEach(date => {
  //     const equipesAffectations = affectations[date];
  //     console.log(affectations);
  
  //     // Créez un objet pour stocker le nombre de personnes par bureau
  //     const personnesParBureau: { [key: string]: number } = {};
  
  //     Object.keys(equipesAffectations).forEach(equipe => {
  //       const bureau = equipesAffectations[equipe];
  //       const equipeDetails = this.equipes.find(e => e.nom === equipe);
  
  //       // Vérifiez si l'équipe existe et si le bureau est occupé par l'équipe
  //       if (equipeDetails && bureau) {
  //         personnesParBureau[bureau] = (personnesParBureau[bureau] || 0) + equipeDetails.nombrePersonnes;
  //       }
  //     });
  
  //     // Itérez sur les bureaux et calculez les places restantes
  //     Object.keys(personnesParBureau).forEach(bureau => {
  //       console.log(personnesParBureau);
  //       const capaciteBureau = this.getBureauCapacite(bureau);
  //       const totalPlacesOccupees = personnesParBureau[bureau];
        
  //       if (capaciteBureau) {
  //         const placesRestantes = capaciteBureau - totalPlacesOccupees;
  //         result.push({
  //           bureau: bureau,
  //           placesRestantes: placesRestantes
  //         });
  //       }
  //     });
  //   });
  
  //   console.log(result);
  //   return result;
  // }

  calculPlacesRestantes(affectations: any): { bureau: string, placesRestantes: number, jour: string }[] {
    const result: { bureau: string, placesRestantes: number, jour: string }[] = [];
  
    Object.keys(affectations).forEach(date => {
      const equipesAffectations = affectations[date];
      date= this.getDayName(date);
      
  
      const personnesParBureau: { [key: string]: number } = {};
  
      Object.keys(equipesAffectations).forEach(equipe => {
        const bureau = equipesAffectations[equipe];
        const equipeDetails = this.equipes.find(e => e.nom === equipe);
  
        if (equipeDetails && bureau) {
          personnesParBureau[bureau] = (personnesParBureau[bureau] || 0) + equipeDetails.nombrePersonnes;
        }
      });
  
      Object.keys(personnesParBureau).forEach(bureau => {
        const capaciteBureau = this.getBureauCapacite(bureau);
        const totalPlacesOccupees = personnesParBureau[bureau];
  
        if (capaciteBureau) {
          const placesRestantes = capaciteBureau - totalPlacesOccupees;
          result.push({
            bureau: bureau,
            placesRestantes: placesRestantes,
            jour: date
          });
        }
      });
    });
    console.log(result);
  
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

    onAffecterBureauxClick() {
      this.affectationService.affecterBureaux().subscribe(result => {
        window.location.reload();
        console.log('Affectations réalisées avec succès', result);
        // Mettez à jour votre UI en conséquence ou affichez un message
      }, error => {
        console.error('Erreur lors de l\'affectation des bureaux', error);
      });
    }

        
}
