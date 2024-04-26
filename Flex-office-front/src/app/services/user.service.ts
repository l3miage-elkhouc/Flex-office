import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  readonly API_URL = "http://localhost:8080";
  readonly ENDPOINT_USERS = "/utilisateurs";
  readonly ENDPOINT_REGISTER = "/register";
  readonly ENDPOINT_LOGIN = "/login";

  constructor(private httpClient: HttpClient) { }

  // Récupère tous les utilisateurs depuis le backend
  getUsers() {
    return this.httpClient.get(this.API_URL + this.ENDPOINT_USERS).pipe(
      catchError(this.handleError)
    );
  }

  // Vérifie si un email existe déjà dans la base de données
  checkEmailExists(email: string) {
    return this.httpClient.get<boolean>(`${this.API_URL}/check-email/${email}`);
  }

   // Envoie une requête de création d'utilisateur
  registerUser(email: string, password: string) {
    return this.httpClient.post<any>(`${this.API_URL}/register`, { email, password });
  }
  
  // Gère les erreurs HTTP en cas de problème lors de la communication avec le backend
  private handleError(error: HttpErrorResponse) {
    console.error('Une erreur s\'est produite:', error);
    return throwError('Une erreur s\'est produite, veuillez réessayer plus tard.');
  }

  // Met à jour le mot de passe d'un utilisateur
  updatePassword(email: string, password: string): Observable<any> {
    const url = `${this. API_URL}/utilisateurs`;
    const body = { email: email, password: password };
    return this.httpClient.post(url, body);
  }
}
