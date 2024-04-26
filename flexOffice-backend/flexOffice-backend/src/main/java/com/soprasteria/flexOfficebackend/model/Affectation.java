package com.soprasteria.flexOfficebackend.model;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDate date; 


    @ManyToOne
    @JoinColumn(name = "idEquipe", referencedColumnName = "id")
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "idBureau", referencedColumnName = "id")
    private Bureau bureau;

    @Column(name = "placesRestantes")
    private int placesRestantes; 


    // Constructeur par défaut
    public Affectation() {
    }

    // Constructeur avec tous les champs
    public Affectation(LocalDate date, Equipe equipe, Bureau bureau) {
        this.date = date;
        this.equipe = equipe;
        this.bureau = bureau;
        this.placesRestantes = placesRestantes;
    }

    /* Getters et Setters*∕
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public int getPlacesRestantes() {
        return placesRestantes;
    }

    public void setPlacesRestantes(int placesRestantes) {
        this.placesRestantes = placesRestantes;
    }
}
