package net.firstinstance.journalapp.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.firstinstance.journalapp.Entity.User;

public interface UserRepo extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);
    
}
