import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';
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
          this.dataLoaded = true;
        });
      }
    );
  }
  
}



// import { Component, OnInit } from '@angular/core';
// import { AuthService } from '../../auth.service';

// @Component({
//   selector: 'app-dashboard',
//   templateUrl: './dashboard.component.html',
//   styleUrls: ['./dashboard.component.css']
// })
// export class DashboardComponent implements OnInit {
//   userName: string = '';
//   equipeName: string = '';

//   constructor(private authService: AuthService) { }

//   ngOnInit(): void {
//     this.authService.getLoggedInUser().subscribe(
//       (userName: string) => {
//         this.userName = userName;
//         // Une fois que le nom d'utilisateur est récupéré, on récupère le nom de l'équipe
//         this.authService.getEquipeName().subscribe(
//           (equipeName: string) => {
//             this.equipeName = equipeName;
//           }
//         );
//       }
//     );
//   }
// }
