package de.codexbella.content;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Season {
   private int apiId;
   private int seasonNumber;
   private String seasonName;
   private int numberOfEpisodes;
   private String overview;
   private String posterPath;
   private List<Episode> episodes = new ArrayList<>();

   private String username = "";
   private Seen seen = Seen.NO;
   private double vote = 5;
}
