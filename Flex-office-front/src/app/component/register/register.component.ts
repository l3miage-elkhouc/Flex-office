import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  submitted = false;
  registrationError = false;
  registrationErrorMessage!: string;


  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }
   // Récupérer les valeurs des champs email et password du formulaire
    const email = this.registerForm.value.email;
    const password = this.registerForm.value.password;
    // Vérifier si l'email existe déjà dans la base de données
    this.userService.checkEmailExists(email).subscribe(
      exists => {
        if (exists) { // Si l'email existe, mettre à jour le mot de passe associé
          this.userService.updatePassword(email, password).subscribe(
            () => {
              // Succès de la mise à jour du mot de passe
            },
            success => {
              this.registrationError = false; // Pas d'erreur d'inscription
              this.registrationErrorMessage = 'Vous êtes bien enregistré !!';
               this.router.navigate(['/dashboard']); // Naviguer vers la page d'accueil

            }
          );
        } else {
          // Si l'email n'existe pas dans la base de données
          this.registrationError = true;
          this.registrationErrorMessage = 'Cet email n\'existe pas dans la base de données.';
        }
      },
      error => {
         // Gestion des erreurs lors de la vérification de l'email
        this.registrationError = true; // Il y a une erreur d'inscription
        this.registrationErrorMessage = 'Une erreur s\'est produite lors de la vérification de l\'email.';
      }
    );
  }
}
