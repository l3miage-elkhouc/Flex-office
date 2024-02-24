package com.soprasteria.flexOfficebackend.model;

public class User {
    private int id;
    private String nom;
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
