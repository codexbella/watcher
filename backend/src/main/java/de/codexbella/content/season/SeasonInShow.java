package de.codexbella.content.season;

import de.codexbella.content.Seen;
import de.codexbella.content.episode.EpisodeInShow;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SeasonInShow {
   private int apiId;
   private int seasonNumber;
   private String name;
   private int numberOfEpisodes;
   private String overview;
   private String posterPath;
   private List<EpisodeInShow> episodes = new ArrayList<>();

   private String username = "";
   private Seen seen = Seen.NO;
   private double vote = 5;
}
