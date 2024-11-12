package com.divakar.journalApp.service;

import com.divakar.journalApp.model.User;
import com.divakar.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public void createUser(User user) {
        userRepository.save(user);
    }
}
