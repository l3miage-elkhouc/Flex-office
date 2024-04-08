        package com.soprasteria.flexOfficebackend.model;

        import java.math.BigDecimal;
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

            
            @Column(name="nombre_personnes")
            private int nombre_personnes;

            @Column(name="taux_presence")
            private BigDecimal taux_presence = BigDecimal.valueOf(60);

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
            public Equipe() {
                this.taux_presence = BigDecimal.valueOf(60);
            }

            public Equipe(int id, String nom) {
                this.id = id;
                this.nom = nom;
                this.taux_presence = BigDecimal.valueOf(60);
            }


            @ElementCollection(fetch = FetchType.EAGER)
            @CollectionTable(name = "jours_de_presence", joinColumns = @JoinColumn(name = "equipe_id"))
            @Column(name = "jour")
            private List<Integer> joursDePresence;
        
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNom() {
                return nom;
            }

            public int getNombrePersonnes(){
                return nombre_personnes;
            }

            public void setNom(String nom) {
                this.nom = nom;
            }
            public void setNombrePersonnes(int nombre_personnes) {
                this.nombre_personnes = nombre_personnes;
            }
            public BigDecimal getTauxPresence() {
                return taux_presence;
            }

            public void setTauxPresence(BigDecimal taux_presence) {
                this.taux_presence = taux_presence;
            }

            public List<Integer> getJoursDePresence() {
                return joursDePresence;
            }

            public void setJoursDePresence(List<Integer> joursDePresence) {
                this.joursDePresence = joursDePresence;
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
