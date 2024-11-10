package com.divakar.journalApp.controller;

import com.divakar.journalApp.model.JournalEntry;
import com.divakar.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAllEntries(){
        return journalEntryService.getAllEntries();
    }

    @GetMapping("/id/{id}")
    public JournalEntry getEntryById(@PathVariable ObjectId id){
        return journalEntryService.getEntryById(id).orElse(null);
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry){

            JournalEntry old = journalEntryService.getEntryById(id).orElse(null);
            if(old != null){
                old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle(): old.getTitle());
                old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : old.getContent());
            }
            journalEntryService.saveEntry(journalEntry);
            return old;

    }

    @PostMapping
    public JournalEntry addEntry(@RequestBody JournalEntry journalEntry){
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return journalEntry;
    }

    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable ObjectId id){
        journalEntryService.deleteById(id);
        return true;
    }

}
