package com.soprasteria.flexOfficebackend.repository;
import com.soprasteria.flexOfficebackend.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;;;

public interface UtilisateurRepository extends CrudRepository<Utilisateur,Integer>  {
    boolean existsByEmail(String email);
    Utilisateur findByEmail(String email);
    
}
