package de.codexbella.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {
   private final UserService userService;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return userService.findByUsername(username)
            .map(userData -> new User(userData.getUsername(), userData.getPassword(),
                  List.of(new SimpleGrantedAuthority("ROLE_USER"))))
            .orElseThrow(() -> new UsernameNotFoundException(username+" not found."));
   }
}
