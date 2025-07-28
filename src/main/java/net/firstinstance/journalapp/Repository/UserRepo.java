package net.firstinstance.journalapp.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.firstinstance.journalapp.Entity.User;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);
    
}
