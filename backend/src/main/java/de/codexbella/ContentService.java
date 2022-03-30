package de.codexbella;

import com.google.gson.Gson;
import de.codexbella.search.SearchResultShows;
import de.codexbella.search.ShowSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ContentService {
   private final RestTemplate restTemplate;

   @Value("${app.api.key}")
   private String apiKey;

   public List<ShowSearchData> searchForShow(String searchTerm) {

      String searchTermForUrl = searchTerm.toLowerCase(Locale.ROOT).replaceAll("\s", "+");

      String response = restTemplate.getForObject(
            "https://api.themoviedb.org/3/search/tv?api_key="+apiKey+"&query="+searchTermForUrl, String.class);
      SearchResultShows results = new Gson().fromJson(response, SearchResultShows.class);
      return results.getShows().stream().toList();
   }
}
