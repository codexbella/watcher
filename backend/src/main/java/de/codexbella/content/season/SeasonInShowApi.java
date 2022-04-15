package de.codexbella.content.season;

import com.google.gson.annotations.SerializedName;
import de.codexbella.content.episode.EpisodeInShowApi;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SeasonInShowApi {
   @SerializedName("id")
   private int apiId;

   @SerializedName("season_number")
   private int seasonNumber;

   @SerializedName("name")
   private String name;

   @SerializedName("episode_count")
   private int numberOfEpisodes;

   private String overview;

   @SerializedName("poster_path")
   private String posterPath;

   @SerializedName("episodes")
   private List<EpisodeInShowApi> episodes = new ArrayList<>();
}
