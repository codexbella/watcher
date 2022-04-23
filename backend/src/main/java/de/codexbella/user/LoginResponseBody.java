package de.codexbella.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponseBody {
   private String token;
   private String language;
}
