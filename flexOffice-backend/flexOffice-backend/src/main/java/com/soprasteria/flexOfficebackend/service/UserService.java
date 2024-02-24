package com.soprasteria.flexOfficebackend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.soprasteria.flexOfficebackend.model.User;

@Service
public class UserService {
    
    static ArrayList <User> users = new ArrayList<>(Arrays.asList(

            new User(1, "ELKHOU", "Chaymae"),
            new User(2, "Bah", "kk"),
            new User(3, "ELKH", "Chay")



    ));

    public List<User> getUsers(){
        return users;
    }
    public User getUser(int id){
        return users.stream().filter(user -> user.getId() == id ).findFirst().orElse(null);
    }
    public void deleteUser(int id){
        users.removeIf(user -> user.getId() == id);
    }

    public void addUser(User user){
        users.add(user);
    }
    public void updateUser(User user, int id){
        users.forEach(user1 -> {
            if(user1.getId() == id){
                users.set(users.indexOf(user1), user);
            }
        });
    }
}
