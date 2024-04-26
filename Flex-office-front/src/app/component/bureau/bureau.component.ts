  import { Component, OnInit } from '@angular/core';
  import { AuthService } from '../../services/auth.service';
  import { BureauService } from '../../services/bureau.service';

  @Component({
    selector: 'app-bureau',
    templateUrl: './bureau.component.html',
    styleUrls: ['./bureau.component.css']
  })
  export class BureauComponent implements OnInit {
    nom: string = '';
    capacite: number = 0;
    showSuccessModal: boolean = false;
    userName: string = '';
    equipeName: string = '';
    dataLoaded: boolean = false;
    admin!: boolean;
    bureaux: any[] = [];
    selectedBureauId!: number;
    selectedBureauNom: string = '';
    selectedBureauCapacite: number = 0;


    constructor(private bureauService: BureauService, private authService: AuthService) { }

    ngOnInit() {
      // Récupération des données utilisateur via le service d'authentification
      this.authService.getUserData().subscribe(([userName, equipeName, admin]) => {
        this.userName = userName;
        this.equipeName = equipeName;
        this.admin = admin;
      });
      this.loadBureaux();
    }

    // Méthode pour ajouter un bureau
    ajouterBureau() {
      this.bureauService.ajouterBureau({ nom: this.nom, capacite: this.capacite }).subscribe({
        next: (data: any) => {
          console.log(data);
          this.showSuccessModal = true;
          this.loadBureaux(); // Recharger les bureaux après en avoir ajouté un nouveau
        },
        error: (error: any) => console.error(error)
      });
    }

    // Méthode pour charger la liste des bureaux
    loadBureaux() {
      this.bureauService.getBureaux().subscribe({
        next: (data) => {
          this.bureaux = data;
          this.dataLoaded = true;
        },
        error: (err) => console.error('Erreur lors du chargement des bureaux:', err)
      });
    }

    // Méthode pour supprimer un bureau
    supprimerBureau(id: number) {
      console.log(id);
      if (confirm('Êtes-vous sûr de vouloir supprimer ce bureau ?')) {
        this.bureauService.supprimerBureau(id).subscribe({
          next: () => {
            console.log('Bureau supprimé avec succès');
            this.loadBureaux(); // Recharger la liste des bureaux après la suppression
          },
          error: (error) => {
            console.error('Erreur lors de la suppression du bureau', error);
          }
        });
      }
    }

    // Méthode pour charger les informations actuelles d'un bureau sélectionné
    loadCurrentBureauInfo() {

      const selectedBureau = this.bureaux.find(bureau => Number(bureau.id) === Number(this.selectedBureauId));
      console.log(selectedBureau);
      if (selectedBureau) {
        this.selectedBureauNom = selectedBureau.nom;
        this.selectedBureauCapacite = selectedBureau.capacite;
      } else {
        this.selectedBureauNom = '';
        this.selectedBureauCapacite = 0;
      }
    }
    

    // Méthode pour mettre à jour les informations d'un bureau
    updateBureau() {
      console.log("ici");
      console.log(this.selectedBureauId);
      console.log(this.selectedBureauCapacite);
      if (this.selectedBureauId && this.selectedBureauCapacite) {
        console.log("oui");
        const id = this.selectedBureauId;
        this.bureauService.updateBureau(id, {capacite: this.selectedBureauCapacite }).subscribe({
          next: (data) => {
            console.log('Bureau mis à jour avec succès', data);
            this.showSuccessModal = true;
            this.loadBureaux(); // Recharger la liste des bureaux après la mise à jour
          },
          error: (error) => console.error('Erreur lors de la mise à jour du bureau', error)
        });
      }
    }
    

    


  }
