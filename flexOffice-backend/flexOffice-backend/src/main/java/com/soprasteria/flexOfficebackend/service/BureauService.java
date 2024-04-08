package com.soprasteria.flexOfficebackend.service;

import com.soprasteria.flexOfficebackend.model.Bureau;
import com.soprasteria.flexOfficebackend.model.Equipe;
import com.soprasteria.flexOfficebackend.repository.BureauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BureauService {

    @Autowired
    private BureauRepository bureauRepository;

    public List<Bureau> getBureaux() {
        List<Bureau> bureaux = new ArrayList<>();
        bureauRepository.findAll().forEach(bureau -> {
            bureaux.add(bureau);
        });
        return bureaux;
    }

    public Bureau getBureau(int id) {
        return bureauRepository.findById(id).orElse(null);
    }

    public void deleteBureau(int id) {
        bureauRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public void addBureau(Bureau bureau) {
        bureauRepository.save(bureau);
    }

    public void updateBureau(Bureau bureau, int id) {
        Bureau bureauExistante = bureauRepository.findById(id).orElseThrow(() -> new RuntimeException("Bureau non trouvée"));
        bureauExistante.setCapacite(bureau.getCapacite());
        bureauRepository.save(bureauExistante);
    }

    public Optional<Bureau> trouverParNom(String nom) {
        return bureauRepository.findByNom(nom); // Supposant que cette méthode est définie dans BureauRepository
    }
    
    
}
