package de.codexbella.content.season;

import com.google.gson.annotations.SerializedName;
import de.codexbella.content.episode.EpisodeApi;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SeasonApi {
   @SerializedName("id")
   private int apiId;
   private String name;
   private String overview;
   @SerializedName("air_date")
   private String airDate;
   @SerializedName("season_number")
   private int seasonNumber;
   @SerializedName("poster_path")
   private String posterPath;
   @SerializedName("episode_count")
   private int numberOfEpisodes;
   private List<EpisodeApi> episodes = new ArrayList<>();
}
