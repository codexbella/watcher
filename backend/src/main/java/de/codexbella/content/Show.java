package de.codexbella.content;

import de.codexbella.content.season.SeasonInShow;
import de.codexbella.content.season.Season;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "shows")
@Data
public class Show {
   @Id
   private String id;
   private int apiId;
   private String name;
   private String overview;
   private String airDate;
   private List<String> originCountry;
   private List<String> languages;
   private String tagline;
   private List<Genre> genres = new ArrayList<>();
   private double voteAverage;
   private int voteCount;
   private String posterPath;
   private List<SeasonInShow> seasonInShows = new ArrayList<>();
   private List<Season> seasons = new ArrayList<>();

   private String username = "";
   private Seen seen = Seen.NO;
   private double vote = 5;
}
