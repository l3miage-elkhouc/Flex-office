package com.soprasteria.flexOfficebackend.controller;

import com.soprasteria.flexOfficebackend.service.AffectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/affectations")
public class AffectationController {

    @Autowired
    private AffectationService affectationService;
    
    @GetMapping
    public ResponseEntity<Map<String, Map<String, String>>> getAffectations() {
        Map<String, Map<String, String>> affectations = affectationService.affecterBureaux();
        return ResponseEntity.ok(affectations);
    }
}
