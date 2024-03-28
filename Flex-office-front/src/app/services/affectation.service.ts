import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // Importez HttpClient
import { Observable } from 'rxjs';
import { AffectationsSemaine } from '../models/affectation.model'; // Ajustez le chemin si nécessaire

@Injectable({
  providedIn: 'root'
})
export class AffectationService {
  private baseUrl = 'http://localhost:8080/affectations'; // URL de votre API

  constructor(private http: HttpClient) { } // Injectez HttpClient

  // Méthode pour récupérer les affectations depuis le backend
  getAffectations(): Observable<AffectationsSemaine> {
    return this.http.get<AffectationsSemaine>(this.baseUrl);
  }

  getAffectationsEquipe(nomEquipe: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/affectations/${nomEquipe}`);
  }
  
}
