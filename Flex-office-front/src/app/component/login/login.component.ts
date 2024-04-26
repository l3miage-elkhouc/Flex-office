import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userEmail: string = '';
  userPassword: string = '';
  loginError: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(): void {
    this.authService.login(this.userEmail, this.userPassword).subscribe(
      (response) => {
        if (response.message === 'Login successful') {
          this.router.navigate(['/dashboard']);
        } else {
          this.loginError = 'Erreur de connexion. Vérifiez vos informations.';
        }
      },
      (error: any) => {
        this.loginError = 'Erreur de connexion. Vérifiez vos informations.';
      }
    );
  }
}


// import { Component } from '@angular/core';
// import { AuthService } from '../../auth.service';
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-login',
//   templateUrl: './login.component.html',
//   styleUrls: ['./login.component.css']
// })
// export class LoginComponent {
//   userEmail: string = '';
//   userPassword: string = '';
//   loginError: string = '';

//   constructor(private authService: AuthService, private router: Router) { }

//   onSubmit(): void {
//     this.authService.login(this.userEmail, this.userPassword).subscribe(
//       (response) => {
//         if (response.message === 'Login successful') {
//           this.authService.getUserNameSubject().next(response.userName);
//           this.router.navigate(['/dashboard']);
//         } else {
//           this.loginError = 'Erreur de connexion. Vérifiez vos informations.';
//         }
//       },
//       (error: any) => {
//         this.loginError = 'Erreur de connexion. Vérifiez vos informations.';
//       }
//     );
//   }
// }




