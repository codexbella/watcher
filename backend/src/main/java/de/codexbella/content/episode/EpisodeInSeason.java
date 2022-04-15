package de.codexbella.content.episode;

import de.codexbella.content.Seen;
import lombok.Data;

@Data
public class EpisodeInSeason {
      private String apiId;
      private String name;
      private String overview;
      private String airDate;
      private String episodeNumber;
      private String seasonNumber;
      private String voteAverage;
      private String voteCount;
      private String stillPath;

      private String username = "";
      private Seen seen = Seen.NO;
      private double vote = 5;
}
