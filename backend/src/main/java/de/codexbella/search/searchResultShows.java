package de.codexbella.search;

import com.google.gson.annotations.SerializedName;
import de.codexbella.content.Show;
import lombok.Data;

import java.util.List;

@Data
public class searchResultShows {
   private int page;

   @SerializedName("results")
   private List<ShowSearchData> shows;

   @SerializedName("total_pages")
   private int numberOfPages;

   @SerializedName("total_results")
   private int numberOfResults;
}
