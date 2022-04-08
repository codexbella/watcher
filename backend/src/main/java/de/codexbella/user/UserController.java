package de.codexbella.user;

import de.codexbella.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
   private final UserService userService;
   private final AuthenticationManager authenticationManager;
   private final JwtService jwtService;

   @PostMapping("/register")
   public ResponseEntity<String> register(@RequestBody RegisterData user) {
      try {
         String creationMessage = userService.createUser(user);
         return new ResponseEntity<>(creationMessage, HttpStatus.CREATED);
      } catch (IllegalStateException | InputMismatchException e) {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
   }

   @PostMapping("/login")
   public ResponseEntity<String> login(@RequestBody LoginData loginData) {
         Authentication auth = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword())
         );
         List<String> roles = auth.getAuthorities().stream().map(ga -> ga.getAuthority()).toList();
         Map<String, Object> claims = new HashMap<>();
         claims.put("roles", roles);
         String token = jwtService.createToken(claims, loginData.getUsername());
         return new ResponseEntity<>(token, HttpStatus.OK);
   }
}
