package de.codexbella.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
   private final JwtService jwtService;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String tokenFromRequest = getAuthToken(request);

      if (tokenFromRequest != null && !tokenFromRequest.isBlank()) {
         try {
            Claims claims = jwtService.extractClaims(tokenFromRequest);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                  claims.getSubject(),
                  "",
                  List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
            filterChain.doFilter(request, response);
         } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            response.setStatus(401);
         }
      } else {
         filterChain.doFilter(request, response);
      }
   }

   private String getAuthToken(HttpServletRequest request) {
      String authHeader = request.getHeader("authorization");
      if (authHeader != null) {
         return authHeader.replace("Bearer", "").trim();
      }
      return null;
   }
}
