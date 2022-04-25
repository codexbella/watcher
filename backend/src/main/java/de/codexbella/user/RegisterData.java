package de.codexbella.user;

import lombok.Data;

@Data
public class RegisterData {
   private String username;
   private String password;
   private String passwordAgain;
   private String language;
}
