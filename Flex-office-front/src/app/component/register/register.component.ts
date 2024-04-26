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

    const email = this.registerForm.value.email;
    const password = this.registerForm.value.password;

    this.userService.checkEmailExists(email).subscribe(
      exists => {
        if (exists) {
          this.userService.updatePassword(email, password).subscribe(
            () => {

            },
            success => {
              this.registrationError = false;
              this.registrationErrorMessage = 'Vous êtes bien enregistré !!';
               this.router.navigate(['/dashboard']); // Naviguer vers la page d'accueil

            }
          );
        } else {
          this.registrationError = true;
          this.registrationErrorMessage = 'Cet email n\'existe pas dans la base de données.';
        }
      },
      error => {
        this.registrationError = true;
        this.registrationErrorMessage = 'Une erreur s\'est produite lors de la vérification de l\'email.';
      }
    );
  }
}
