package com.soprasteria.flexOfficebackend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="equipe")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="nom")
    private String nom;
    @JsonIgnore
    @OneToMany(mappedBy = "equipe")

    private List<Utilisateur> utilisateurs;

    @ManyToMany
    @JoinTable(
            name = "equipe_bureau",
            joinColumns = @JoinColumn(name = "equipe_id"),
            inverseJoinColumns = @JoinColumn(name = "bureau_id"))
    private List<Bureau> bureaux;
    

    // Constructeurs, getters et setters
    public Equipe() {}

    public Equipe(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public List<Bureau> getBureaux() {
        return bureaux;
    }

    public void setBureaux(List<Bureau> bureaux) {
        this.bureaux = bureaux;
    }
}
