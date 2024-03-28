import { Component, OnInit } from '@angular/core';
import { AffectationService } from '../../services/affectation.service';
import { AffectationsSemaine } from '../../models/affectation.model';
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

  constructor(private affectationService: AffectationService) { }

  ngOnInit() {
    this.affectationService.getAffectations().subscribe(data => {
      this.affectationsSemaine = data;
      this.transformData(data);
    });
  }

  transformData(data: any) {
    const days = Object.keys(data); // ['Lundi', 'Mardi', ...]
    const teams = new Set<string>();

    // Find all unique teams
    days.forEach(day => {
      Object.keys(data[day]).forEach(team => {
        teams.add(team);
      });
    });

    // Create the transformed structure
    teams.forEach(team => {
      const teamAffectations: { equipe: string; [key: string]: string } = { equipe: team };
      days.forEach(day => {
        teamAffectations[day] = data[day][team] || 'Non attribué'; // Use 'Non attribué' if no assignment for that day
      });
      this.transformedAffectations.push(teamAffectations);
    });
  }
}
