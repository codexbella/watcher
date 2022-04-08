package de.codexbella;

import de.codexbella.content.ShowApi;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<ShowApi, String> {
}
