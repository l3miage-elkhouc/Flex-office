// import { Component, OnInit } from '@angular/core';
// import { Equipe } from '../../equipe';
// import { Bureau } from '../../bureau';
// import { EquipeService } from '../../equipe.service';

// @Component({
//   selector: 'app-affectation',
//   templateUrl: './affectation.component.html',
//   styleUrls: ['./affectation.component.css']
// })
// export class AffectationComponent implements OnInit {
//   equipes!: Equipe[];
//   bureaux!: Bureau[];

//   constructor(private equipeService: EquipeService) {}

//   ngOnInit(): void {
//     this.getEquipes();
//     this.getBureaux();
//   }

//   getEquipes(): void {
//     this.equipeService.getEquipes().subscribe((equipes: any) => this.equipes = equipes);
//   }

//   getBureaux(): void {
//     this.equipeService.getBureaux().subscribe((bureaux : any) => this.bureaux = bureaux);
//   }

//   // Méthode pour trouver un bureau disponible pour une équipe
//   private trouverBureauDisponible(bureauxDisponibles: string[]): string | null {
//     // Vérifie que la liste des bureaux disponibles n'est pas vide
//     if (bureauxDisponibles.length > 0) {
//       // Choix aléatoire d'un bureau parmi la liste des bureaux disponibles
//       const randIndex = Math.floor(Math.random() * bureauxDisponibles.length);
//       return bureauxDisponibles[randIndex];
//     }
//     return null; // Aucun bureau disponible
//   }
// }
// affectation.component.ts

import { Component, OnInit } from '@angular/core';
import { Equipe } from '../../equipe';
import { Bureau } from '../../bureau';
import { EquipeService } from '../../equipe.service';

@Component({
  selector: 'app-affectation',
  templateUrl: './affectation.component.html',
  styleUrls: ['./affectation.component.css']
})
export class AffectationComponent implements OnInit {
  equipes!: Equipe[];
  bureaux!: Bureau[];

  constructor(private equipeService: EquipeService) {}

  ngOnInit(): void {
    this.getEquipes();
    this.getBureaux();
  }

  getEquipes(): void {
    this.equipeService.getEquipes().subscribe((equipes: any) => {
      this.equipes = equipes.map((equipe: Equipe) => ({
        ...equipe,
        bureauAttribue: this.bureaux ? this.trouverBureauPourEquipe(equipe) : null // Vérifier si bureaux est défini
      }));
    });
  }
  

  getBureaux(): void {
    this.equipeService.getBureaux().subscribe((bureaux: Bureau[]) => {
      this.bureaux = bureaux; // Affectation des bureaux récupérés à la propriété bureaux
    });
  }

  // private trouverBureauPourEquipe(equipe: Equipe): string | null {
  //   // Vérifiez s'il y a des bureaux disponibles
  //   if (this.bureaux.length > 0) {
  //     // Obtenez les noms des bureaux disponibles
  //     const nomsBureauxDisponibles = this.bureaux.map(bureau => bureau.nom);
  
  //     // Choisissez un bureau au hasard parmi les bureaux disponibles
  //     const randIndex = Math.floor(Math.random() * nomsBureauxDisponibles.length);
  //     return nomsBureauxDisponibles[randIndex];
  //   } else {
  //     // Aucun bureau disponible
  //     return null;
  //   }
  // }
  
  
  // public trouverBureauPourEquipe(equipe: Equipe): string | null {
  //   // Vérifiez s'il y a des bureaux disponibles
  //   if (this.bureaux.length > 0) {
  //     // Obtenez les noms des bureaux disponibles
  //     const nomsBureauxDisponibles = this.bureaux.map(bureau => bureau.nom);
  
  //     // Choisissez un bureau au hasard parmi les bureaux disponibles
  //     const randIndex = Math.floor(Math.random() * nomsBureauxDisponibles.length);
  //     return nomsBureauxDisponibles[randIndex];
  //   } else {
  //     // Aucun bureau disponible
  //     return null;
  //   }
  // }
  
  public trouverBureauPourEquipe(equipe: Equipe): string | null {
    // Vérifiez s'il y a des bureaux disponibles
    if (this.bureaux.length > 0) {
      // Obtenez les noms des bureaux disponibles
      const nomsBureauxDisponibles = this.bureaux.map(bureau => bureau.nom);
  
      // Obtenez les noms des bureaux déjà attribués
      const nomsBureauxAttribues = this.equipes.filter(e => e.bureauAttribue).map(e => e.bureauAttribue!);
  
      // Filtrez les bureaux disponibles en retirant ceux qui sont déjà attribués à d'autres équipes
      const bureauxDisponibles = nomsBureauxDisponibles.filter(bureauNom => !nomsBureauxAttribues.includes(bureauNom));
  
      // Choisissez un bureau au hasard parmi les bureaux disponibles
      if (bureauxDisponibles.length > 0) {
        const randIndex = Math.floor(Math.random() * bureauxDisponibles.length);
        const bureauAttribue = bureauxDisponibles[randIndex];
  
        // Retirez le bureau attribué de la liste des bureaux disponibles
        const bureau = this.bureaux.find(b => b.nom === bureauAttribue);
        if (bureau) {
          bureau.equipeAttribue = equipe.nom;
        }
  
        return bureauAttribue;
      }
    }
  
    // Aucun bureau disponible
    return null;
  }
  
  
  
  
  
  
}
