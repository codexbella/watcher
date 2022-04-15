package de.codexbella.content.season;

import com.google.gson.annotations.SerializedName;
import de.codexbella.content.episode.EpisodeInSeasonApi;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SeasonApi {
   @SerializedName("id")
   private String apiId;
   @SerializedName("name")
   private String name;
   @SerializedName("overview")
   private String overview;
   @SerializedName("air_date")
   private String airDate;
   @SerializedName("season_number")
   private String seasonNumber;
   @SerializedName("poster_path")
   private String posterPath;
   @SerializedName("episodes")
   private List<EpisodeInSeasonApi> episodes = new ArrayList<>();
}
