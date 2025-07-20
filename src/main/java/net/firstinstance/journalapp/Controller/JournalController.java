package net.firstinstance.journalapp.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        User user = userService.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found.");
        }
        List<JournalEntity> journalEntries = user.getJournalEntries();
        if (journalEntries == null || journalEntries.isEmpty()) {
            return ResponseEntity.status(404).body("No journal entries found for the user.");
        }
        return ResponseEntity.ok(journalEntries);
    }


    @PostMapping("/save/{username}")
    public ResponseEntity<?> createJournalEntryByUsername(@RequestBody JournalEntity journalEntry,@PathVariable String username) {
        try{
        journalService.saveJournalEntry(journalEntry, username);
        return ResponseEntity.ok("Journal entry created successfully");
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating journal entry: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{myId}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId myId) {
        journalService.deleteJournalEntryById(myId);
        return true;
    }

    @GetMapping("/get/{myId}")
    public JournalEntity GetJournalEntrybyId(@PathVariable ObjectId myId) {
        return journalService.findUsingId(myId).orElse(null);
    }
    
    @PutMapping("/update/{myId}")
    public  JournalEntity UpdatedJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntity journalEntry) {
        JournalEntity oldEntry = journalService.findUsingId(myId).orElse(null);
        if(oldEntry != null) {
            oldEntry.setTitle(journalEntry.getTitle()!= null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle() :  oldEntry.getTitle());
            oldEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : oldEntry.getContent());
        }

        return journalEntry;
    }
}
