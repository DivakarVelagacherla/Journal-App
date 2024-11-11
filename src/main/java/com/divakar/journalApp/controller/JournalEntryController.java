package com.divakar.journalApp.controller;

import com.divakar.journalApp.model.JournalEntry;
import com.divakar.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<?> getAllEntries(){
        return new ResponseEntity<>(journalEntryService.getAllEntries(), HttpStatus.OK);
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

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry){

            JournalEntry old = journalEntryService.getEntryById(id).orElse(null);
            if(old != null){
                old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle(): old.getTitle());
                old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : old.getContent());
            }
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(old,HttpStatus.CREATED);

    }

    @PostMapping
    public ResponseEntity<?> addEntry(@RequestBody JournalEntry journalEntry){
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id){
        if(journalEntryService.deleteById(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
