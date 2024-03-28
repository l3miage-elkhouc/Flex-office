// src/app/models/affectation.model.ts

export interface AffectationJour {
    [equipe: string]: string;
  }
  
  export interface AffectationsSemaine {
    [jour: string]: AffectationJour;
  }
  