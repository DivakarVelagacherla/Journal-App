package com.divakar.journalApp.controller;

import com.divakar.journalApp.model.JournalEntry;
import com.divakar.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public JournalEntry getAllEntries(){
        return new JournalEntry();
    }

    @GetMapping("/{id}")
    public JournalEntry getEntryById(@PathVariable String id){
        return new JournalEntry();
    }

    @PutMapping
    public boolean updateEntry(JournalEntry journalEntry){
        return true;
    }

    @PostMapping
    public boolean addEntry(@RequestBody JournalEntry journalEntry){
        journalEntry.setDate(new Date());
        journalEntryService.saveEntry(journalEntry);
        return true;
    }

}
