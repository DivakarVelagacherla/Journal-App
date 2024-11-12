package com.divakar.journalApp.controller;

import com.divakar.journalApp.model.JournalEntry;
import com.divakar.journalApp.model.User;
import com.divakar.journalApp.service.JournalEntryService;
import com.divakar.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllEntriesOfUser(@PathVariable String userName){
        User user = userService.getUserByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id){

        Optional<JournalEntry> fetchedJournal = journalEntryService.getEntryById(id);
        if(fetchedJournal.isPresent()){
            return new ResponseEntity<>(fetchedJournal.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry, @PathVariable String userName){

            JournalEntry old = journalEntryService.getEntryById(id).orElse(null);
            if(old != null){
                old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle(): old.getTitle());
                old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : old.getContent());
            }
            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(old,HttpStatus.CREATED);

    }

    @PostMapping("{userName}")
    public ResponseEntity<?> addEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName){

        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry, userName);
        return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{userName}/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id, @PathVariable String userName){
        if(journalEntryService.deleteById(id, userName)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
