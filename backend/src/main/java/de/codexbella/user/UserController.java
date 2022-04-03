package de.codexbella.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
   private final UserService userService;

   @PostMapping("/register")
   public ResponseEntity<String> register(@RequestBody RegisterData user) {
      try {
         String creationMessage = userService.createUser(user);
         return new ResponseEntity<>(creationMessage, HttpStatus.CREATED);
      } catch (Exception e) {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
   }
}
