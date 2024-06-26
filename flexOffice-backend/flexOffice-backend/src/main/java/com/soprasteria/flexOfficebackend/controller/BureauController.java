package com.soprasteria.flexOfficebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soprasteria.flexOfficebackend.model.Bureau;
import com.soprasteria.flexOfficebackend.repository.BureauRepository;
import com.soprasteria.flexOfficebackend.service.BureauService;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
public class BureauController {

    @Autowired
    private BureauService bureauService;

    @RequestMapping("/bureaux")
    public List<Bureau> getBureaux() {
        return bureauService.getBureaux();
    }

    @RequestMapping("/bureau/{id}")
    public Bureau getBureau(@PathVariable int id) {
        return bureauService.getBureau(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/bureau/{id}")
    public void deleteBureau(@PathVariable int id) {
        bureauService.deleteBureau(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/bureaux")
    public void addBureau(@RequestBody Bureau bureau) {
        bureauService.addBureau(bureau);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/bureaux/{id}")
    public void updateBureau(@RequestBody Bureau bureau, @PathVariable int id) {
        bureauService.updateBureau(bureau, id);
    }

    @Autowired
    private BureauRepository bureauRepository;

    @PostMapping
    public ResponseEntity<Bureau> ajouterBureau(@RequestBody Bureau bureau) {
        Bureau nouveauBureau = bureauRepository.save(bureau);
        return ResponseEntity.ok(nouveauBureau);
    }
    
    @DeleteMapping("/bureaux/{id}")
    public ResponseEntity<?> deleteBureau(@PathVariable Integer id) {
        bureauService.deleteBureau(id);
        return ResponseEntity.ok().build(); // Retourne une réponse HTTP 200 OK sans contenu
    }
    

}
