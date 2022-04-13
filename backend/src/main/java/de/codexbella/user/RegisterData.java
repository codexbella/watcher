package de.codexbella.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class RegisterData {
   private String username;
   private String password;
   private String passwordAgain;
}
