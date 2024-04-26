import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AffectationService } from '../../services/affectation.service';
import { AffectationsSemaine } from '../../models/affectation.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userName: string = '';
  equipeName: string = '';
  dataLoaded: boolean = false;
  admin!: boolean ;
  affectationsSemaine: AffectationsSemaine | undefined;
  debutSemaine: string | undefined;
  finSemaine: string | undefined;

  constructor(private authService: AuthService, private affectationService: AffectationService) { }

  ngOnInit(): void {
    this.authService.getUserData().subscribe(
      ([userName, equipeName, admin]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.admin=admin;
        // Récupérer les affectations pour l'équipe de l'utilisateur
        this.affectationService.getAffectationsEquipe(equipeName).subscribe(affectations => {
          this.affectationsSemaine = affectations;
          this.calculerIntervalleSemaine(affectations); 
          this.dataLoaded = true; //Indiquer que les données sont chargées
        });

      }
    );
  }

  // Méthode pour calculer l'intervalle de la semaine à partir des affectations
  calculerIntervalleSemaine(affectations: AffectationsSemaine): void {
    const jours = Object.keys(affectations); // Extrait les clés qui sont des strings représentant des dates
    if (jours.length > 0) {
        // Utilisation de la première date comme référence pour déterminer la semaine
        const dateReference = new Date(jours[0]);

        // Trouver le lundi de la semaine de la date de référence
        const debutSemaine = new Date(dateReference);
        debutSemaine.setDate(debutSemaine.getDate() - debutSemaine.getDay() + (debutSemaine.getDay() === 0 ? -6 : 1));

        // Trouver le vendredi de la même semaine
        const finSemaine = new Date(debutSemaine);
        finSemaine.setDate(debutSemaine.getDate() + 4);

        // Formatage des dates en français
        const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
        this.debutSemaine = debutSemaine.toLocaleDateString('fr-FR', options); // Lundi
        this.finSemaine = finSemaine.toLocaleDateString('fr-FR', options); // Vendredi
    }
}

  // Méthode pour obtenir le nom du jour à partir d'une chaîne de date ISO
  getDayName(dateIsoString: string): string {
    const date = new Date(dateIsoString);
    return new Intl.DateTimeFormat('fr-FR', { weekday: 'long' }).format(date);
  }
}
