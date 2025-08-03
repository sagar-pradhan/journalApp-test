package net.firstinstance.journalapp.Service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.firstinstance.journalapp.Entity.JournalEntity;
import net.firstinstance.journalapp.Entity.User;
import net.firstinstance.journalapp.Repository.JournalRepo;
import net.firstinstance.journalapp.Repository.UserRepo;

@Component
public class UserService {

    @Autowired
    private UserRepo userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JournalRepo journalRepository;

    public void saveUserEntry(User user) {
        // Encode password before saving
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }
    
    public boolean updateUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            // Only encode password if it's being changed
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public void deleteUserEntryById(ObjectId id) {
        // First find the user to get their journal entries
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Delete all journal entries associated with this user
            List<JournalEntity> journals = user.getJournalEntries();
            if (journals != null && !journals.isEmpty()) {
                for (JournalEntity journal : journals) {
                    journalRepository.deleteById(journal.getId());
                }
            }
        }
        // Finally delete the user
        userRepository.deleteById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUsingId(ObjectId myId) {
        return userRepository.findById(myId);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}