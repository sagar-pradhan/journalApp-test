package net.firstinstance.journalapp.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.firstinstance.journalapp.Entity.JournalEntity;
import net.firstinstance.journalapp.Entity.User;
import net.firstinstance.journalapp.Repository.JournalRepo;

@Service
public class JournalService {
    
    @Autowired
    private JournalRepo journalRepo;    //Intecting JournalRepo to the Service class
    
    @Autowired
    private UserService userService;    //Injecting UserService to the Service class

    public boolean saveJournalEntry(JournalEntity journalEntry, String username) {
        try {
            User user = userService.findByUserName(username);
            if (user == null) {
                return false; // User not found
            }
            
            // Validate journal entry
            if (journalEntry.getTitle() == null || journalEntry.getTitle().trim().isEmpty()) {
                return false; // Title is required
            }
            
            journalEntry.setDate(LocalDateTime.now());
            journalEntry.setContent(journalEntry.getContent() != null ? journalEntry.getContent().trim() : "");
            JournalEntity saved = journalRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUserEntry(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteJournalEntryById(ObjectId id, String username) {
        try {
            User user = userService.findByUserName(username);
            if (user == null) {
                return false;
            }
            
            // Remove from user's journal list first
            user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
            userService.saveUserEntry(user);
            
            // Then delete the journal entry
            journalRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteJournalEntryById(ObjectId id) {
        journalRepo.deleteById(id);
    }

    public List<JournalEntity> findAll() {
        return journalRepo.findAll();
    }



    public Optional<JournalEntity> findUsingId(ObjectId myId) {
        return journalRepo.findById(myId);
    }

    public void updateJournalEntry(JournalEntity oldEntry) {
        journalRepo.save(oldEntry);
    }


}