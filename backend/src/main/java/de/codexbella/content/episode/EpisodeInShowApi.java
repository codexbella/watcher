package de.codexbella.content.episode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EpisodeInShowApi {
   @SerializedName("id")
   private int apiId;

   @SerializedName("season_number")
   private int seasonNumber;

   @SerializedName("episode_number")
   private int episodeNumber;

   private String name;

   private String overview;

   @SerializedName("vote_average")
   private double voteAverage;

   @SerializedName("vote_count")
   private int voteCount;

   @SerializedName("still_path")
   private String stillPath;
}
