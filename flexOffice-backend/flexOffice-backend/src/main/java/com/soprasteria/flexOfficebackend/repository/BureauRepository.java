package com.soprasteria.flexOfficebackend.repository;

import com.soprasteria.flexOfficebackend.model.Bureau;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/*  Définit les méthodes pour l'accès aux données des bureaux dans la base de données.*/
public interface BureauRepository extends CrudRepository<Bureau, Integer> {

    Optional<Bureau> findByNom(String nom);
}
