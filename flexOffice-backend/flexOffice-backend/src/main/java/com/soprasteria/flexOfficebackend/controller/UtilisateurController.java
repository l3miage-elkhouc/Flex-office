 package com.soprasteria.flexOfficebackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.soprasteria.flexOfficebackend.model.Utilisateur;
import com.soprasteria.flexOfficebackend.repository.UtilisateurRepository;
import com.soprasteria.flexOfficebackend.service.UtilisateurService;


    @CrossOrigin(origins="*")
    @RestController
    public class UtilisateurController {
        
        @Autowired
        private UtilisateurService userService;


        @RequestMapping("/utilisateurs")
        public List<Utilisateur> getUser(){
            return userService.getUtilisateurs();
        }
        
        @RequestMapping("/Utilisateur/{id}")
        public Utilisateur getUser(@PathVariable int id){
            return userService.getUtilisateur(id);
        }

        @RequestMapping(method = RequestMethod.DELETE , value = "/Utilisateur/{id}") 
        public void deleteUtilisateur(@PathVariable int id){
            userService.deleteUtilisateur(id);
        }

        // @RequestMapping(method = RequestMethod.POST , value = "/utilisateurs")
        // public void addUtilisateur(@RequestBody Utilisateur utilisateur){
        //     userService.addUtilisateur(utilisateur);
        // }

        @RequestMapping(method = RequestMethod.POST, value = "/utilisateurs")
        public ResponseEntity<String> addUtilisateur(@RequestBody Utilisateur utilisateur) {
            String email = utilisateur.getEmail();
            String password = utilisateur.getPassword();
        
            // Vérifier si l'e-mail existe déjà dans la base de données
            Utilisateur existingUser = userService.getUtilisateurByEmail(email);
            if (existingUser != null) {
                // Mettre à jour le mot de passe de l'utilisateur existant
                existingUser.setPassword(password);
                userService.updateUtilisateur(existingUser, existingUser.getId());
        
                return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
            }
        
            // L'utilisateur n'existe pas dans la base de données
            return new ResponseEntity<>("Email address not found", HttpStatus.NOT_FOUND);
        }
        
        
        


        @RequestMapping(method = RequestMethod.PUT , value = "/utilisateur/{id}")
        public void updateUtilisateur(@RequestBody Utilisateur utilisateur , @PathVariable int id){
            userService.updateUtilisateur(utilisateur,id);
        }

    //     @PostMapping("/utilisateurs/{id}/adminStatus")
    //     public ResponseEntity<?> updateAdminStatus(@PathVariable int id, @RequestBody Map<String, Boolean> update) {
    //     boolean isAdmin = update.getOrDefault("admin", false);
    //     userService.updateAdminStatus(id, isAdmin);
    //     return ResponseEntity.ok().build();
    // }

    // Dans votre UserController ou un contrôleur similaire
@Autowired
private UtilisateurRepository utilisateurRepository;

@PostMapping("/utilisateurs/{id}/admin")
public ResponseEntity<?> updateAdminStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> payload) {
    Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    utilisateur.setAdmin(payload.get("admin"));
    utilisateurRepository.save(utilisateur);
    return ResponseEntity.ok().build();
}



    }
