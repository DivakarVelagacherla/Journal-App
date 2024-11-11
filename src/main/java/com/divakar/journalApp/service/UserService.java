package com.divakar.journalApp.service;

import com.divakar.journalApp.model.Users;
import com.divakar.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public void createUser(Users user) {
        userRepository.save(user);
    }
}
