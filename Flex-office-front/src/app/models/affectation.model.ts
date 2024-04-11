// src/app/models/affectation.model.ts

// export interface AffectationJour {
//     [equipe: string]: string;
//   }
  
//   export interface AffectationsSemaine {
//     [jour: string]: AffectationJour;
//   }

  export interface AffectationJour {
    [equipe: string]: string | string[]; // Peut être une chaîne ou un tableau de chaînes
  }

  export interface AffectationsSemaine {
    [jour: string]: AffectationJour;
  }


  export interface Affectation {
    date: string;
    bureau: string;
  }
  export interface TeamAffectation {
    equipe: string;
    Lundi?: string;
    Mardi?: string;
    Mercredi?: string;
    Jeudi?: string;
    Vendredi?: string;
  }
  