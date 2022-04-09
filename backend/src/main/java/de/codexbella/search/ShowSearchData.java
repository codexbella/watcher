package de.codexbella.search;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShowSearchData {
   @SerializedName("id")
   private int apiId;
   private String name;
   private String overview;
   @SerializedName("first_air_date")
   private String airDate;
   @SerializedName("original_language")
   private String language;
   @SerializedName("vote_average")
   private double voteAverage;
   @SerializedName("vote_count")
   private int voteCount;
   @SerializedName("poster_path")
   private String posterPath;

   private boolean liked = false;
}
