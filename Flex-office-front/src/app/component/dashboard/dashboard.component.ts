import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userName: string = '';
  equipeName: string = '';
  dataLoaded: boolean = false;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.getUserData().subscribe(
      ([userName, equipeName]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.dataLoaded = true; // Marquer les données comme chargées
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
