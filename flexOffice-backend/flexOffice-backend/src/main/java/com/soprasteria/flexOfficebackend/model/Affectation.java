package com.soprasteria.flexOfficebackend.model;

import jakarta.persistence.*;

@Entity
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "jour")
    private String jour;

    @ManyToOne
    @JoinColumn(name = "idEquipe", referencedColumnName = "id")
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "idBureau", referencedColumnName = "id")
    private Bureau bureau;

    // Constructeur par défaut
    public Affectation() {
    }

    // Constructeur avec tous les champs
    public Affectation(String jour, Equipe equipe, Bureau bureau) {
        this.jour = jour;
        this.equipe = equipe;
        this.bureau = bureau;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Bureau getBureau() {
        return bureau;
    }

    public void setBureau(Bureau bureau) {
        this.bureau = bureau;
    }
}
