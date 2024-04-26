package com.soprasteria.flexOfficebackend.controller;

import com.soprasteria.flexOfficebackend.model.Utilisateur;
import com.soprasteria.flexOfficebackend.service.UtilisateurService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;


    
    // @PostMapping("/login")
    // public ResponseEntity<Object> login(@RequestBody Utilisateur user) {
    //     String email = user.getEmail();
    //     String password = user.getPassword();

    //     // Vérifier si l'utilisateur existe dans la base de données
    //     if (utilisateurService.existsByEmail(email)) {
    //         Utilisateur existingUser = utilisateurService.getUtilisateurByEmail(email);
    //         // Vérifier si le mot de passe correspond
    //         if (existingUser.getPassword().equals(password)) {
    //             // Créer un objet JSON contenant le message de succès et le nom de l'utilisateur
    //             String responseJson = "{\"message\": \"Login successful\", \"userName\": \"" + existingUser.getNom() + "\"}";
    //             return ResponseEntity.ok().body(responseJson);
    //         } else {
    //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid email or password\"}");
    //         }
    //     } else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid email or password\"}");
    //     }
    // }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Utilisateur user) {
    String email = user.getEmail();
    String password = user.getPassword();

    // Vérifier si l'utilisateur existe dans la base de données
    if (utilisateurService.existsByEmail(email)) {
        Utilisateur existingUser = utilisateurService.getUtilisateurByEmail(email);
        // Vérifier si le mot de passe correspond
        if (existingUser.getPassword().equals(password)) {
            // Ajouter le nom de l'équipe à la réponse JSON
            String equipeName = existingUser.getEquipe().getNom(); // Supposant que vous avez une relation entre Utilisateur et Équipe
            Boolean admin = existingUser.getAdmin();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "Login successful");
            responseData.put("userName", existingUser.getNom());
            responseData.put("equipeName", equipeName);
            responseData.put("admin", admin);

            return ResponseEntity.ok().body(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid email or password\"}");
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid email or password\"}");
    }
}


}
