package com.soprasteria.flexOfficebackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user")

public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @Column(name="nom")
    private String nom;

    @Column(name="prenom")
    private String prenom;


    public User(){

    }
    public User(int id ,String nom ,String prenom ){
        super();
        this.id = id ;
        this.nom = nom;
        this.prenom = prenom;

    }

    public int getId(){
        return id;
    }
    public String getNom(){
        return nom;
    }
    public String getPrenom(){
        return prenom;
    }

    public void setId(int id){
        this.id =id;
    }

    public void setNom(String nom){
        this.nom =nom;
    }

    public void setId(String prenom){
        this.prenom =prenom;
    }
}
