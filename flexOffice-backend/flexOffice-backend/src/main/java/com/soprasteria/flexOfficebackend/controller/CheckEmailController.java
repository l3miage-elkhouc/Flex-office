package com.soprasteria.flexOfficebackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soprasteria.flexOfficebackend.service.UtilisateurService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/check-email")
public class CheckEmailController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = utilisateurService.checkEmailExists(email);
        return ResponseEntity.ok(exists);
    }
}
