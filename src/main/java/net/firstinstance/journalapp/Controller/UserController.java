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
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
        try{
            // Check for validation errors
            if (result.hasErrors()) {
                StringBuilder errors = new StringBuilder();
                result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
                return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
            }
            
            // Check if user already exists
            User existingUser = userService.findByUserName(user.getUsername());
            if (existingUser != null) {
                return ResponseEntity.status(409).body("Username already exists");
            }
            
            userService.saveUserEntry(user);
            return ResponseEntity.status(201).body("User created successfully");
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    
    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result, @PathVariable String username) {
        // Check for validation errors
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
        }
        
        user.setUsername(username); // Ensure we update the correct user
        boolean updated = userService.updateUser(user);
        if (updated) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @DeleteMapping("/delete/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
        try{
            Optional<User> user = userService.findUsingId(myId);
            if (user.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }
            userService.deleteUserEntryById(myId);
            return ResponseEntity.ok("User deleted successfully");
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
}
    