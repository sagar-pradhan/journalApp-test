package net.firstinstance.journalapp.Entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "journal")
@Data //prjoect lombok to generate getters and setters
@NoArgsConstructor
public class JournalEntity {
    @Id
    private ObjectId id;

    private String title;
    
    private String content;

    private LocalDateTime date;

}
