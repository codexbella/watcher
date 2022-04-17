package de.codexbella.content.episode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EpisodeApi {
      @SerializedName("id")
      private int apiId;
      private String name;
      private String overview;
      @SerializedName("air_date")
      private String airDate;
      @SerializedName("episode_number")
      private int episodeNumber;
      @SerializedName("season_number")
      private int seasonNumber;
      @SerializedName("vote_average")
      private double voteAverage;
      @SerializedName("vote_count")
      private int voteCount;
      @SerializedName("still_path")
      private String stillPath;
}
