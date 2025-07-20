package net.firstinstance.journalapp.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.firstinstance.journalapp.Entity.JournalEntity;

@Repository
public interface JournalRepo extends MongoRepository<JournalEntity, ObjectId> {
    

}
