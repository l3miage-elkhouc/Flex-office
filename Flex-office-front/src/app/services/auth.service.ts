import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject, combineLatest } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';
  private loggedInUserSubject: BehaviorSubject<string> = new BehaviorSubject<string>(localStorage.getItem('loggedInUser') || '');
  private equipeNameSubject: BehaviorSubject<string> = new BehaviorSubject<string>(localStorage.getItem('equipeName') || '');
  private adminSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(localStorage.getItem('admin') === 'true');
  private dataLoadedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap((response: any) => {
          this.loggedInUserSubject.next(response.userName);
          this.equipeNameSubject.next(response.equipeName);
          this.adminSubject.next(response.admin);
          this.dataLoadedSubject.next(true); // Marquer les données comme chargées

          // Enregistrer les informations d'authentification dans le localStorage
          localStorage.setItem('loggedInUser', response.userName);
          localStorage.setItem('equipeName', response.equipeName);
          localStorage.setItem('admin', response.admin);
        }),
        catchError(this.handleError)
      );
  }

  // Combiner les deux observables pour n'émettre les données qu'une seule fois
  getUserData(): Observable<[string, string, boolean]> {
    return combineLatest([this.loggedInUserSubject, this.equipeNameSubject, this.adminSubject]).pipe(
      catchError(this.handleError)
    );
  }

  getDataLoaded(): Observable<boolean> {
    return this.dataLoadedSubject.asObservable();
  }

  logout(): void {
    // Supprimer les informations d'authentification du localStorage lors de la déconnexion
    localStorage.removeItem('loggedInUser');
    localStorage.removeItem('equipeName');
    localStorage.removeItem('admin');

    // Réinitialiser les sujets BehaviorSubject
    this.loggedInUserSubject.next('');
    this.equipeNameSubject.next('');
    this.adminSubject.next(false);
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Une erreur est survenue lors de la connexion.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Une erreur est survenue : ${error.error.message}`;
    } else {
      errorMessage = `Code d'erreur : ${error.status}, message : ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
