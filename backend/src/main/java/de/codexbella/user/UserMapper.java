package de.codexbella.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
   public UserData toUserDocument(RegisterData registerData) {
      UserData userDocument = new UserData();
      userDocument.setUsername(registerData.getUsername());
      userDocument.setPassword(registerData.getPassword());
      return userDocument;
   }
}
