package de.codexbella;

import de.codexbella.content.Show;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowRepository extends MongoRepository<Show, String> {
   Optional<Show> findByApiIdAndUsername(int apiId, String username);
}
