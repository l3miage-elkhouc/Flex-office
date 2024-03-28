package com.soprasteria.flexOfficebackend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.soprasteria.flexOfficebackend.model.Affectation;


public interface AffectationRepository extends CrudRepository<Affectation, Long> {

    @Query("SELECT a FROM Affectation a WHERE a.equipe.nom = :nomEquipe")
    List<Affectation> findByNomEquipe(String nomEquipe);

}
