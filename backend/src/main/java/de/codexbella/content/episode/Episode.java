package de.codexbella.content.episode;

import de.codexbella.content.Seen;
import lombok.Data;

@Data
public class Episode {
      private int apiId;
      private String name;
      private String overview;
      private String airDate;
      private int episodeNumber;
      private int seasonNumber;
      private double voteAverage;
      private int voteCount;
      private String stillPath;

      private String username = "";
      private Seen seen = Seen.NO;
      private int rating = 0;
}
