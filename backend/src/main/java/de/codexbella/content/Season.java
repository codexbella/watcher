package de.codexbella.content;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Season {
   @SerializedName("id")
   private int apiId;

   @SerializedName("season_number")
   private int seasonNumber;

   @SerializedName("name")
   private String seasonName;

   @SerializedName("episode_count")
   private int numberOfEpisodes;

   private String overview;

   @SerializedName("poster_path")
   private String posterPath;

   @SerializedName("episodes")
   private List<Episode> episodes = new ArrayList<>();
}
