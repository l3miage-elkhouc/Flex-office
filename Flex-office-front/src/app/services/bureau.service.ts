// bureau.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BureauService {
  private apiUrl = 'http://localhost:8080/bureaux';

  constructor(private http: HttpClient) { }

  //ajout d'un nouveau bureau
  ajouterBureau(bureau: any): Observable<any> {
    return this.http.post(this.apiUrl, bureau);
  }

  // Suppression d'un bureau
  supprimerBureau(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
    // return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
  
  //Récupérer les bureaux
  getBureaux(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  // Ajoutez cette méthode
// Dans bureau.service.ts
  updateBureau(id: number, bureau: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, bureau);
  }

  
}
