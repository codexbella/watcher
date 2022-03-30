package de.codexbella.content;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "shows")
@Data
public class Show {
   @SerializedName("id")
   private int apiId;

   private String name;

   private String overview;

   @SerializedName("first_air_date")
   private String airDate;

   @SerializedName("origin_country")
   private List<String> originCountry;

   @SerializedName("languages")
   private List<String> languages;

   private String tagline;

   private List<Genre> genres = new ArrayList<>();

   @SerializedName("vote_average")
   private double voteAverage;

   @SerializedName("vote_count")
   private int voteCount;

   @SerializedName("poster_path")
   private String posterPath;

   private List<Season> seasons = new ArrayList<>();
}
