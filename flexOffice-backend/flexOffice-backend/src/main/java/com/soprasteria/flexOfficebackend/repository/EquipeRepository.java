package com.soprasteria.flexOfficebackend.repository;

import com.soprasteria.flexOfficebackend.model.Equipe;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface EquipeRepository extends CrudRepository<Equipe, Integer> {

    Optional<Equipe> findByNom(String nom);
}
