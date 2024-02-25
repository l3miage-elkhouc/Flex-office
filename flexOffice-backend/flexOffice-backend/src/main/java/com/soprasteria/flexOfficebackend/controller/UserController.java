package com.soprasteria.flexOfficebackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soprasteria.flexOfficebackend.model.User;
import com.soprasteria.flexOfficebackend.service.UserService;

@CrossOrigin(origins="*")
@RestController
public class UserController {
    
    // @RequestMapping("/hello")
    // public String HelloWorld(){
    //     return "Hello World !!" ;
    // }
    @Autowired
    private UserService userService;


    @RequestMapping("/users")
    public List<User> getUser(){
        return userService.getUsers();
    }
    
    @RequestMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        return userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.DELETE , value = "/user/{id}") 
    public void deleteUser(@PathVariable int id){
        userService.deleteUser(id);
    }

    @RequestMapping(method = RequestMethod.POST , value = "/users")
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }

    @RequestMapping(method = RequestMethod.PUT , value = "/user/{id}")
    public void updateUser(@RequestBody User user , @PathVariable int id){
        userService.updateUser(user,id);
    }


}
