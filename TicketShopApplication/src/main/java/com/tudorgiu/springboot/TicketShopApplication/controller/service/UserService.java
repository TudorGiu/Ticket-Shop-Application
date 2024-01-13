package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.controller.dao.RoleRepository;
import com.tudorgiu.springboot.TicketShopApplication.controller.dao.UserRepository;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Role;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void save(User newUser) {

        // Encode the user's password before saving
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Set the user's role (assumes role with ID 1 exists)
        int id = 1;

        try {
            newUser.addRole(roleRepository.findById(id).get());
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Role with id " + id + " not found.");
        }

        // Save the user
        userRepository.save(newUser);
    }

    public User getByEmail(String email){

        // Retrieve a list of all users
        List<User> users = userRepository.findAll();

        // Iterate through the list of users to find the one with the specified email
        for(User userPivot : userRepository.findAll())
            if(userPivot.getEmail().equals(email))
                return userPivot;

        // Throw an exception if no user is found with the given email
        throw new NoSuchElementException("User with email " + email + " does not exist.");
    }
}
