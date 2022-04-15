package de.codexbella.content.episode;

import de.codexbella.content.Seen;
import lombok.Data;

@Data
public class EpisodeInShow {
   private int apiId;
   private int seasonNumber;
   private int episodeNumber;
   private String name;
   private String overview;
   private double voteAverage;
   private int voteCount;
   private String stillPath;

   private String username = "";
   private Seen seen = Seen.NO;
   private double vote = 5;
}
