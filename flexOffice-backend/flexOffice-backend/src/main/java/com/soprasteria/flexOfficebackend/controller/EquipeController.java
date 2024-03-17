package com.soprasteria.flexOfficebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.service.EquipeService;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @RequestMapping("/equipes")
    public List<Equipe> getEquipes() {
        return equipeService.getEquipes();
    }

    @RequestMapping("/equipe/{id}")
    public Equipe getEquipe(@PathVariable int id) {
        return equipeService.getEquipe(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/equipe/{id}")
    public void deleteEquipe(@PathVariable int id) {
        equipeService.deleteEquipe(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/equipes")
    public void addEquipe(@RequestBody Equipe equipe) {
        equipeService.addEquipe(equipe);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/equipe/{id}")
    public void updateEquipe(@RequestBody Equipe equipe, @PathVariable int id) {
        equipeService.updateEquipe(equipe, id);
    }
}
