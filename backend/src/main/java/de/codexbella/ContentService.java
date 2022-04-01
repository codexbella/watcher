package de.codexbella;

import com.google.gson.Gson;
import de.codexbella.search.SearchResultShows;
import de.codexbella.search.ShowSearchData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ContentService {
   private final RestTemplate restTemplate;
   private final String apiKey;

   public ContentService(@Value("${app.api.key}") String apiKey, RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
      this.apiKey = apiKey;
   }

   public List<ShowSearchData> searchForShow(String searchTerm) {
      String response = restTemplate.getForObject(
            "https://api.themoviedb.org/3/search/tv?api_key="+apiKey+"&query="+searchTerm, String.class);
      SearchResultShows results = new Gson().fromJson(response, SearchResultShows.class);
      return results.getShows().stream().toList();
   }
}
