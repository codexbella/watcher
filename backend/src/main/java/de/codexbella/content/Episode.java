package de.codexbella.content;

import lombok.Data;

@Data
public class Episode {
   private int apiId;
   private int seasonNumber;
   private int episodeNumber;
   private String name;
   private String overview;
   private double voteAverage;
   private int voteCount;
   private String stillPath;
}
