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

import net.firstinstance.journalapp.Entity.User;
import net.firstinstance.journalapp.Service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try{
        userService.saveUserEntry(user);
        return ResponseEntity.status(200).body("User created successfully");
        }catch (Exception e) {
            return ResponseEntity.status(404).body("Error creating user: " + e.getMessage());
        }
    }

    
    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username) {
        User userdb = userService.findByUserName(username);
        if(userdb != null) {
            userdb.setPassword(user.getPassword());
            userdb.setUsername(user.getUsername());
            userService.saveUserEntry(userdb);
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @DeleteMapping("/delete/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
        try{
        userService.deleteUserEntryById(myId);
        return ResponseEntity.status(200).body("User deleted");
        }catch(Exception e){
            return ResponseEntity.status(404).body("Error deleting user: " + e.getMessage());
        }
    }
}
    