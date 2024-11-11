package com.divakar.journalApp.controller;

import com.divakar.journalApp.model.JournalEntry;
import com.divakar.journalApp.model.Users;
import com.divakar.journalApp.service.JournalEntryService;
import com.divakar.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/username/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName){
        Users user = userService.getUserByUserName(userName);
        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Users user){
        if(userService.getUserByUserName(user.getUserName()) == null ){
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Users user){
        Users userInDb = userService.getUserByUserName(user.getUserName());
        if(userInDb != null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.createUser(userInDb);
            return new ResponseEntity<>("User Updated", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }



}
