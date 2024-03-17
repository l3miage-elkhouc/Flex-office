import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject, combineLatest } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';
  private loggedInUserSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');
  private equipeNameSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');
  private dataLoadedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap((response: any) => {
          this.loggedInUserSubject.next(response.userName);
          this.equipeNameSubject.next(response.equipeName);
          this.dataLoadedSubject.next(true); // Marquer les données comme chargées
        }),
        catchError(this.handleError)
      );
  }

  // Combiner les deux observables pour n'émettre les données qu'une seule fois
  getUserData(): Observable<[string, string]> {
    return combineLatest([this.loggedInUserSubject, this.equipeNameSubject]).pipe(
      catchError(this.handleError)
    );
  }

  getDataLoaded(): Observable<boolean> {
    return this.dataLoadedSubject.asObservable();
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


// import { Injectable } from '@angular/core';
// import { HttpClient, HttpErrorResponse } from '@angular/common/http';
// import { Observable, throwError, BehaviorSubject, Subject } from 'rxjs';
// import { catchError, tap } from 'rxjs/operators';

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {
//   private apiUrl = 'http://localhost:8080';
//   private loggedInUserSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');
//   private equipeNameSubject = new Subject<string>();

//   constructor(private http: HttpClient) { }

//   login(email: string, password: string): Observable<any> {
//     return this.http.post<any>(`${this.apiUrl}/login`, { email, password })
//       .pipe(
//         tap((response: any) => {
//           this.loggedInUserSubject.next(response.userName);
//           this.equipeNameSubject.next(response.equipeName);
//         }),
//         catchError(this.handleError)
//       );
//   }

//   getLoggedInUser(): Observable<string> {
//     return this.loggedInUserSubject.asObservable();
//   }

//   getEquipeName(): Observable<string> {
//     return this.equipeNameSubject.asObservable();
//   }

//   private handleError(error: HttpErrorResponse): Observable<never> {
//     let errorMessage = 'Une erreur est survenue lors de la connexion.';
//     if (error.error instanceof ErrorEvent) {
//       errorMessage = `Une erreur est survenue : ${error.error.message}`;
//     } else {
//       errorMessage = `Code d'erreur : ${error.status}, message : ${error.message}`;
//     }
//     console.error(errorMessage);
//     return throwError(errorMessage);
//   }
// }










