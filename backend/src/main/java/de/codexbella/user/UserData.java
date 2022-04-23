package de.codexbella.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class UserData {

   @Id
   private String id;
   private String username;
   private String password;
   private String language;
}
