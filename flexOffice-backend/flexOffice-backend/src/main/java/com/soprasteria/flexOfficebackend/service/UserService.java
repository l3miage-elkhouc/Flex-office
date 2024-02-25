package com.soprasteria.flexOfficebackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soprasteria.flexOfficebackend.model.User;
import com.soprasteria.flexOfficebackend.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            users.add(user);
        });
        return users;
    }
    public User getUser(int id){
        return userRepository.findById(id).orElse(null);
    }
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

    public void addUser(User user){
        userRepository.save(user);
    }
    public void updateUser(User user, int id){
        userRepository.save(user);
    }
}
