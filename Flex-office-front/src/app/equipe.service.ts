
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Equipe } from './equipe';
import { Bureau } from './bureau';

@Injectable({
  providedIn: 'root'
})
export class EquipeService {
  getUtilisateurs() {
    throw new Error('Method not implemented.');
  }
  private apiUrl = 'http://localhost:8080'; // Modifier l'URL en fonction de votre API

  constructor(private http: HttpClient) {}

  getEquipes(): Observable<Equipe[]> {
    return this.http.get<Equipe[]>(`${this.apiUrl}/equipes`);
  }

  getBureaux(): Observable<Bureau[]> {
    return this.http.get<Bureau[]>(`${this.apiUrl}/bureaux`);
  }
}
