package com.divakar.journalApp.service;

import com.divakar.journalApp.model.User;
import com.divakar.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public void createUser(User user) {
        if (!user.getPassword().startsWith("{bcrypt}")) { // Avoid double encoding
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void deleteUserByUserName(String userName){
        userRepository.deleteUserByUserName(userName);
    }
}
