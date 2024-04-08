import { Component, OnInit } from '@angular/core';
import { AffectationService } from '../../services/affectation.service';
import { AffectationsSemaine } from '../../models/affectation.model'; // Assurez-vous que le chemin est correct

@Component({
  selector: 'app-affectation',
  templateUrl: './affectation.component.html',
  styleUrls: ['./affectation.component.css']
})
export class AffectationComponent implements OnInit {
  affectationsSemaine: AffectationsSemaine | undefined;
  debutSemaine: string | undefined;
  finSemaine: string | undefined;

  constructor(private affectationService: AffectationService) { }

  ngOnInit() {
    this.affectationService.getAffectations().subscribe(data => {
      this.affectationsSemaine = data;
      this.calculerIntervalleSemaine(data);
      });
  }

    calculerIntervalleSemaine(affectations: AffectationsSemaine): void {
      const jours = Object.keys(affectations); // Extrait les clés qui sont des strings représentant des dates
      if (jours.length > 0) {
        const dates = jours.map(jour => new Date(jour));
        dates.sort((a, b) => a.getTime() - b.getTime()); // Trie les dates
        const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
        this.debutSemaine = dates[0].toLocaleDateString('fr-FR', options); // Première date
        this.finSemaine = dates[dates.length - 1].toLocaleDateString('fr-FR', options); // Dernière date
      }
    }
}
