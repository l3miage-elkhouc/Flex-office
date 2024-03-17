// package com.soprasteria.flexOfficebackend.model;

// import jakarta.persistence.*;

// @Entity
// @Table(name="affectation")
// public class Affectation {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name="id")
//     private int id;

//     @ManyToOne
//     @JoinColumn(name="equipe_id")
//     private Equipe equipe;

//     @ManyToOne
//     @JoinColumn(name="bureau_id")
//     private Bureau bureau;

//     @Column(name="jour")
//     private int jour;

//     // Constructeurs, getters et setters
//     public Affectation() {}

//     public Affectation(Equipe equipe, Bureau bureau, int jour) {
//         this.equipe = equipe;
//         this.bureau = bureau;
//         this.jour = jour;
//     }

//     public int getId() {
//         return id;
//     }

//     public void setId(int id) {
//         this.id = id;
//     }

//     public Equipe getEquipe() {
//         return equipe;
//     }

//     public void setEquipe(Equipe equipe) {
//         this.equipe = equipe;
//     }

//     public Bureau getBureau() {
//         return bureau;
//     }

//     public void setBureau(Bureau bureau) {
//         this.bureau = bureau;
//     }

//     public int getJour() {
//         return jour;
//     }

//     public void setJour(int jour) {
//         this.jour = jour;
//     }
// }
