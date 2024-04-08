  // bureau.service.ts
  import { Injectable } from '@angular/core';
  import { HttpClient } from '@angular/common/http';
  import { Observable } from 'rxjs';

  @Injectable({
    providedIn: 'root'
  })
  export class EquipeService {
    private apiUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) { }
    //Récupérer les bureaux
    getEquipes(): Observable<any> {
      return this.http.get(`${this.apiUrl}/equipes`);
    }

    //Récupérer la liste des utilisateurs
    getUtilisateurs(): Observable<any> {
      return this.http.get(`${this.apiUrl}/utilisateurs`);
    }

    //Ajouter une equipe
    ajouterEquipe(equipe: any): Observable<any> {
      return this.http.post(`${this.apiUrl}/addEquipes`, equipe);
  }

  //Modification des droits d'un utilisateur
  updateAdminStatus(userId: number, isAdmin: boolean): Observable<any> {
      return this.http.post(`${this.apiUrl}/utilisateurs/${userId}/admin`, { admin: isAdmin });
  }

  //Modicication du nombre de personnes d'une équipe
  updateEquipe(equipeId: number, equipeData: any): Observable<any> {
      return this.http.put(`${this.apiUrl}/equipes/${equipeId}`, equipeData);
    }

    updateEquipeDays(equipeId: number, joursDePresence: number[]): Observable<any> {
      return this.http.put(`${this.apiUrl}/equipes/${equipeId}/joursDePresence`,joursDePresence);
    }

    getEquipeDetails(equipeId: number): Observable<any> {
      return this.http.get(`${this.apiUrl}/equipe/${equipeId}`);
    }

    // equipe.service.ts
  deleteEquipe(equipeId: number): Observable<any> {
      return this.http.delete(`${this.apiUrl}/equipes/${equipeId}`);
    }
    
    
    


  }
