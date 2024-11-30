package com.divakar.journalApp.controller;

import com.divakar.journalApp.model.JournalEntry;
import com.divakar.journalApp.model.User;
import com.divakar.journalApp.service.JournalEntryService;
import com.divakar.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllEntriesOfUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userService.getUserByUserName(userName);
            List<JournalEntry> journalEntries = user.getJournalEntries();
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getUserByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());

        if (!journalEntryList.isEmpty()) {
            Optional<JournalEntry> fetchedJournal = journalEntryService.getEntryById(id);
            if (fetchedJournal.isPresent()) {
                return new ResponseEntity<>(fetchedJournal.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUserByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();

        if (!journalEntryList.isEmpty()) {
            JournalEntry old = journalEntryService.getEntryById(id).orElse(null);
            if (old == null) {
                return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
            } else {

                old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : old.getTitle());
                old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old, userName);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> addEntry(@RequestBody JournalEntry journalEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry, userName);
        return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        if (journalEntryService.deleteById(id, userName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
