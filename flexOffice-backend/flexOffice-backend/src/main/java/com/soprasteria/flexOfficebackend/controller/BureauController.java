package com.soprasteria.flexOfficebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.soprasteria.flexOfficebackend.model.Bureau;
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

    @RequestMapping(method = RequestMethod.PUT, value = "/bureau/{id}")
    public void updateBureau(@RequestBody Bureau bureau, @PathVariable int id) {
        bureauService.updateBureau(bureau, id);
    }
}
