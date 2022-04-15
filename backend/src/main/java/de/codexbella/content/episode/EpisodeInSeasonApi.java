package de.codexbella.content.episode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EpisodeInSeasonApi {
      @SerializedName("id")
      private String apiId;
      @SerializedName("name")
      private String name;
      @SerializedName("overview")
      private String overview;
      @SerializedName("air_date")
      private String airDate;
      @SerializedName("episode_number")
      private String episodeNumber;
      @SerializedName("season_number")
      private String seasonNumber;
      @SerializedName("vote_average")
      private String voteAverage;
      @SerializedName("vote_count")
      private String voteCount;
      @SerializedName("still_path")
      private String stillPath;
}
