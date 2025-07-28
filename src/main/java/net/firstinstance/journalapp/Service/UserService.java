package net.firstinstance.journalapp.Service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.firstinstance.journalapp.Entity.User;
import net.firstinstance.journalapp.Repository.UserRepo;

@Component
public class UserService {

    @Autowired
    private UserRepo userRepository;

    public void saveUserEntry(User user) {
        userRepository.save(user);
    }

    public void deleteUserEntryById(ObjectId id) {
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
}