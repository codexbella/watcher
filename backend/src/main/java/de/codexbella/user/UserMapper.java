package de.codexbella.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
   public UserData registerDataToUserData(RegisterData registerData) {
      UserData userData = new UserData();
      userData.setUsername(registerData.getUsername());
      userData.setPassword(registerData.getPassword());
      return userData;
   }
}
