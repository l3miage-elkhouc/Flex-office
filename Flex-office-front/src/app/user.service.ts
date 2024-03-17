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

  getUsers() {
    return this.httpClient.get(this.API_URL + this.ENDPOINT_USERS).pipe(
      catchError(this.handleError)
    );
  }

  checkEmailExists(email: string) {
    return this.httpClient.get<boolean>(`${this.API_URL}/check-email/${email}`);
  }

  registerUser(email: string, password: string) {
    return this.httpClient.post<any>(`${this.API_URL}/register`, { email, password });
  }

  login(user: any) {
    return this.httpClient.post(this.API_URL + this.ENDPOINT_LOGIN, user).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Une erreur s\'est produite:', error);
    return throwError('Une erreur s\'est produite, veuillez réessayer plus tard.');
  }

  updatePassword(email: string, password: string): Observable<any> {
    const url = `${this. API_URL}/utilisateurs`;
    const body = { email: email, password: password };
    return this.httpClient.post(url, body);
  }
}
