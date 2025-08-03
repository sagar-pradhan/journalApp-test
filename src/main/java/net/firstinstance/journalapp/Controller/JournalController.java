package net.firstinstance.journalapp.Controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.firstinstance.journalapp.Entity.JournalEntity;
import net.firstinstance.journalapp.Entity.User;
import net.firstinstance.journalapp.Service.JournalService;
import net.firstinstance.journalapp.Service.UserService;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    //small bug fixed, but cannot access the journalentries, i believe that there is no database made for this but when i push "user/get" can access it
    @GetMapping("/get/user/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        User userservice = userService.findByUserName(username);
        if (userservice == null) {
            return ResponseEntity.status(404).body("User not found.");
        }
        List<JournalEntity> journalEntries = userservice.getJournalEntries();
        if (journalEntries == null || journalEntries.isEmpty()) {
            return ResponseEntity.status(404).body("No journal entries found for the user.");
        }
        return ResponseEntity.ok(journalEntries);
    }


    @PostMapping("/save/{username}")
    public ResponseEntity<?> createJournalEntryByUsername(@Valid @RequestBody JournalEntity journalEntry, BindingResult result, @PathVariable String username) {
        try{
            // Check for validation errors
            if (result.hasErrors()) {
                StringBuilder errors = new StringBuilder();
                result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
                return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
            }
            
            // Validate username
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            
            boolean saved = journalService.saveJournalEntry(journalEntry, username);
            if (saved) {
                return ResponseEntity.status(201).body("Journal entry created successfully");
            } else {
                return ResponseEntity.status(400).body("Failed to create journal entry. User may not exist.");
            }
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating journal entry: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        try {
            Optional<JournalEntity> journalEntry = journalService.findUsingId(myId);
            if (journalEntry.isEmpty()) {
                return ResponseEntity.status(404).body("Journal entry not found");
            }
            journalService.deleteJournalEntryById(myId);
            return ResponseEntity.ok("Journal entry deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting journal entry: " + e.getMessage());
        }
    }

    @GetMapping("/get/{myId}")
    public ResponseEntity<?> GetJournalEntrybyId(@PathVariable ObjectId myId) {
        try {
            Optional<JournalEntity> journalEntry = journalService.findUsingId(myId);
            if (journalEntry.isPresent()) {
                return ResponseEntity.ok(journalEntry.get());
            } else {
                return ResponseEntity.status(404).body("Journal entry not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving journal entry: " + e.getMessage());
        }
    }
    
    @PutMapping("/update/{myId}")
    public ResponseEntity<?> UpdatedJournalById(@PathVariable ObjectId myId, @Valid @RequestBody JournalEntity journalEntry, BindingResult result) {
        try {
            // Check for validation errors
            if (result.hasErrors()) {
                StringBuilder errors = new StringBuilder();
                result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
                return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
            }
            
            JournalEntity oldEntry = journalService.findUsingId(myId).orElse(null);
            if(oldEntry == null) {
                return ResponseEntity.status(404).body("Journal entry not found");
            }
            
            // Update only non-null and non-empty fields
            if(journalEntry.getTitle() != null && !journalEntry.getTitle().trim().isEmpty()) {
                oldEntry.setTitle(journalEntry.getTitle().trim());
            }
            if(journalEntry.getContent() != null && !journalEntry.getContent().trim().isEmpty()) {
                oldEntry.setContent(journalEntry.getContent().trim());
            }
            
            journalService.updateJournalEntry(oldEntry);
            return ResponseEntity.ok(oldEntry); // Return the updated entity, not the request body
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating journal entry: " + e.getMessage());
        }
    }
}