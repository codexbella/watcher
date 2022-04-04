package de.codexbella.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class JwtService {
   private final String secret;

   public JwtService(@Value("${app.jwt.secret}") String secret) {
      this.secret = secret;
   }

   public String createToken(Map<String, Object> claims, String username) {
      System.out.println(secret);
      return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(2))))
            .signWith(SignatureAlgorithm.HS256, "secret")
            .compact();
   }

   public Claims extractClaims(String token) {
      return Jwts.parser()
            .setSigningKey("secret")
            .parseClaimsJws(token)
            .getBody();
   }
}
