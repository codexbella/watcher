package de.codexbella.content.season;

import de.codexbella.content.Seen;
import de.codexbella.content.episode.EpisodeInSeason;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Season {
   private String apiId;
   private String name;
   private String overview;
   private String airDate;
   private String seasonNumber;
   private String posterPath;
   private List<EpisodeInSeason> episodes = new ArrayList<>();

   private String username = "";
   private Seen seen = Seen.NO;
   private double vote = 5;
}
