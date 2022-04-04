package de.codexbella.search;

import com.google.gson.annotations.SerializedName;
import de.codexbella.content.Genre;
import de.codexbella.content.Season;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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
}
