package de.codexbella.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserData, String> {
   Optional<UserData> findByUsernameIgnoreCase(String username);
}
