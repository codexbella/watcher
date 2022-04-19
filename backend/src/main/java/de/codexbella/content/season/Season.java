package de.codexbella.content.season;

import de.codexbella.content.Seen;
import de.codexbella.content.episode.Episode;
import lombok.Data;

import java.util.List;

@Data
public class Season {
   private int apiId;
   private String name;
   private String overview;
   private String airDate;
   private int seasonNumber;
   private String posterPath;
   private int numberOfEpisodes;
   private List<Episode> episodes;

   private String username = "";
   private Seen seen = Seen.NO;
   private int rating = 0;
}
