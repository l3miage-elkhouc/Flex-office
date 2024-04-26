import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { EquipeService } from '../../services/equipe.service';
import { Utilisateur } from '../../models/utilisateur.model';

@Component({
  selector: 'app-equipe',
  templateUrl: './equipe.component.html',
  styleUrl: './equipe.component.css'
})
export class EquipeComponent implements OnInit {
  nom: string = '';
  nombrePersonnes: number = 0;
  userName: string = '';
  equipeName: string = '';
  dataLoaded: boolean = false;
  admin!: boolean;
  transformedAffectations: any[] = [];
  equipes: any[] = [];
  utilisateurs: any[] = [];
  // Pour la sélection et la mise à jour des equipes
  selectedEquipeId!: number;
  selectedEquipeNom: string = '';
  selectedEquipeCapacite: number = 0;
  selectedUtilisateurId!: number;
  selectedUtilisateurNom: string = '';
  showSuccessModal: boolean = false;
  selectedUserIsAdmin: boolean = false;
  selectedUser: Utilisateur | null = null;
  selectedUserId: number | null = null;
  selectedEquipe: any = null;
  jours: string[] = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi'];
  joursSelectionnes: boolean[] = new Array(this.jours.length).fill(false);



  constructor(private equipeService: EquipeService, private authService: AuthService,) { }

  ngOnInit() {
    this.authService.getUserData().subscribe(
      ([userName, equipeName, admin]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.admin=admin;}
    )
    this.loadEquipes();
    this.loadUtilisateurs();
  
  }


  ajouterEquipe() {
    console.log("Ajout d'équipe avec nom:", this.nom, "et nombrePersonnes:", this.nombrePersonnes);
    const equipe = { nom: this.nom, nombrePersonnes: this.nombrePersonnes }; 
    this.equipeService.ajouterEquipe(equipe).subscribe({
        next: (data: any) => {
        console.log("Réponse du serveur:", data);
        this.showSuccessModal = true;
        this.loadEquipes();
      },
      error: (error: any) => console.error(error)
    });
  }
  
  loadEquipes() {
    this.equipeService.getEquipes().subscribe({
      next: (data: any[]) => {
        this.equipes = data;
        this.dataLoaded = true;
      },
      error: (err) => console.error('Erreur lors du chargement des équipes:', err)
    });
  }

    loadUtilisateurs() {
    this.equipeService.getUtilisateurs().subscribe({
      next: (utilisateurs: any[]) => {
        this.utilisateurs = utilisateurs.filter(utilisateur => utilisateur.nom !== this.userName);
        this.dataLoaded = true;
      },
      error: (err) => console.error('Erreur lors du chargement des utilisateurs:', err)
    });
  }

  
  loadCurrentEquipeInfo() {
    this.selectedEquipe = this.equipes.find(equipe => equipe.id === +this.selectedEquipeId) || null;
    
    if (this.selectedEquipe) {
      // Préremplir le nombre de personnes
      this.nombrePersonnes = this.selectedEquipe.nombrePersonnes;
    } else {
      // Réinitialiser le nombre de personnes si l'équipe n'est pas trouvée
      this.nombrePersonnes = 0;
    }
    console.log(this.selectedEquipeId);
    console.log("Equipes:", this.equipes);
    console.log("Selected equipe:", this.selectedEquipe);
  }
  

  loadCurrentUserInfo() {
    const selectedUtilisateur = this.equipes.find(utilisateur => utilisateur.id === this.selectedUtilisateurId);
    if (selectedUtilisateur) {
      this.selectedUtilisateurNom = selectedUtilisateur.nom;
    } else {
      this.selectedUtilisateurNom = '';
    }
  }

  onUserSelected(event: any) {
    console.log("onUserSelected called with value:", event.target.value);
    // Trouver l'utilisateur sélectionné par son ID
    const userId = event.target.value;
    this.selectedUser = this.utilisateurs.find(u => u.id == userId) || null;
    
    // Mettre à jour l'état du checkbox en fonction du statut d'administrateur de l'utilisateur sélectionné
    this.selectedUserIsAdmin = this.selectedUser ? this.selectedUser.admin : false;
  }
  

  updateUserRights() {    
    if (this.selectedUser) {
      console.log(`Mise à jour des droits pour l'utilisateur ID: ${this.selectedUser.id}, Admin: ${this.selectedUserIsAdmin}`);
        this.equipeService.updateAdminStatus(this.selectedUser.id, this.selectedUserIsAdmin).subscribe({
            next: () => {
                console.log('Statut d\'administrateur mis à jour');
                //  rafraîchir la liste des utilisateurs
                this.loadUtilisateurs();
            },
            error: error => console.error('Erreur lors de la mise à jour du statut d\'administrateur', error)
        });
    }
}

updateEquipe() {
  console.log("Mise à jour de l'équipe avec ID:", this.selectedEquipeId, "Nombre de personnes:", this.nombrePersonnes);
  if (this.selectedEquipeId && this.nombrePersonnes != null) {
    this.equipeService.updateEquipe(this.selectedEquipeId, { nombrePersonnes: this.nombrePersonnes })
      .subscribe({
        next: () => {
          console.log('Nombre de personnes mis à jour avec succès.');
          this.loadEquipes(); // Recharger les informations des équipes après la mise à jour
          // Réinitialiser le formulaire 
          this.nombrePersonnes = 0; 
        },
        error: (error) => console.error('Erreur lors de la mise à jour de l\'équipe', error)
      });
  } else {
    console.error("L'ID de l'équipe ou le nombre de personnes n'est pas défini.");
  }
}



loadCurrentEquipeDays() {
  if (this.selectedEquipeId) {
    this.equipeService.getEquipeDetails(this.selectedEquipeId).subscribe({
      next: (equipe) => {
        // Réinitialiser tous les jours à non sélectionnés
        this.joursSelectionnes.fill(false);
        // Cocher les jours récupérés
        equipe.joursDePresence.forEach((jourId: number) => {
          this.joursSelectionnes[jourId - 1] = true;
        });
      },
      error: (err) => console.error('Erreur lors du chargement des jours de présence:', err)
    });
  }
}

/**
 *  mettre à jour les jours de présence d'une équipe en fonction des jours sélectionnés par l'utilisateur. 
 */

updateEquipeDays() {
  const joursDePresence = this.jours
    .filter((_, i) => this.joursSelectionnes[i])
    .map(jour => this.jours.indexOf(jour) + 1);
    console.log(joursDePresence);

  this.equipeService.updateEquipeDays(this.selectedEquipeId, joursDePresence)
    .subscribe({
      next: () => console.log('Jours de présence mis à jour'),
      error: err => console.error('Erreur lors de la mise à jour des jours', err)
    });
}

  
confirmDeletion() {
  if (this.selectedEquipeId) {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette équipe ?")) {
      this.equipeService.deleteEquipe(this.selectedEquipeId).subscribe({
        next: () => {
          alert("Équipe supprimée avec succès.");
          this.loadEquipes(); // Recharge la liste des équipes
        },
        error: (error) => {
          console.error("Erreur lors de la suppression de l'équipe", error);
          alert("Une erreur est survenue lors de la suppression.");
        }
      });
    }
  } else {
    alert("Veuillez sélectionner une équipe à supprimer.");
  }
}

}