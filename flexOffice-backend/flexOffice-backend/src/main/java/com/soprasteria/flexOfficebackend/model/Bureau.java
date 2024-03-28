package com.soprasteria.flexOfficebackend.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="bureau")
public class Bureau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="nom")
    private String nom;

    @Column(name="capacite")
    private int capacite;

    @ManyToMany(mappedBy = "bureaux")
    private List<Equipe> equipes;

    // Constructeurs, getters et setters
    public Bureau() {}

    public Bureau(int id, String nom) {
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

    public int getCapacite(){

        return capacite;
    }
    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }
}
