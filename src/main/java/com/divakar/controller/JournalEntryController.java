package com.divakar.controller;

import com.divakar.model.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {


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
    public boolean addEntry(JournalEntry journalEntry){
        return true;
    }

}
