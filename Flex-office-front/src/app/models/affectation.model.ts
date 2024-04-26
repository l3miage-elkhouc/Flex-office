export interface AffectationJour {
  [equipe: string]: string | string[]; // Peut être une chaîne ou un tableau de chaînes
}

export interface AffectationsSemaine {
  [jour: string]: AffectationJour;
}

export interface CapaciteRestanteJour {
  [bureau: string]: number; // Nombre de places restantes pour chaque bureau
}

export interface CapacitesRestantesSemaine {
  [jour: string]: CapaciteRestanteJour;
}

export interface Affectation {
  date: string;
  bureau: string;
}

export interface TeamAffectation {
  equipe: string;
  Lundi?: string | string[];
  Mardi?: string | string[];
  Mercredi?: string | string[];
  Jeudi?: string | string[];
  Vendredi?: string | string[];
}

export interface PlacesDisponibles {
  [date: string]: {
    [bureau: string]: number; // nombre de places disponibles par bureau
  }
}
