package com.soprasteria.flexOfficebackend.repository;

import com.soprasteria.flexOfficebackend.model.Bureau;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface BureauRepository extends CrudRepository<Bureau, Integer> {

    Optional<Bureau> findByNom(String nom);
}
