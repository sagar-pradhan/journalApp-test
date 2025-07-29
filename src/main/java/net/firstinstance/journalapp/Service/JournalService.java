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

    public void saveJournalEntry(JournalEntity journalEntry, String username) {
        User user = userService.findByUserName(username);
        journalEntry.setDate(LocalDateTime.now());
        journalEntry.setContent(journalEntry.getContent() != null ? journalEntry.getContent() : " ");
        JournalEntity saved = journalRepo.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUserEntry(user);
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