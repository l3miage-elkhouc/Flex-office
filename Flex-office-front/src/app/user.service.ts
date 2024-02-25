import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  readonly  API_URL = "http://localhost:8080"

  readonly ENDPOINT_USERS = "/users"
  
  constructor(private httpClient : HttpClient) { }

  getUsers(){
    return this.httpClient.get(this.API_URL+this.ENDPOINT_USERS);
  }
}
