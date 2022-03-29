package de.codexbella;

import com.google.gson.Gson;
import de.codexbella.search.searchResultShows;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ContentService {
   private final RestTemplate restTemplate;

   public void showSearch(@Value("${app.api.key}") String apiKey, String searchTerm) {

      String searchTermForUrl = searchTerm.toLowerCase(Locale.ROOT).replaceAll("\s", "+");

      String response = restTemplate.getForObject(
            "https://api.themoviedb.org/3/search/tv?api_key="+apiKey+"&query="+searchTermForUrl, String.class);
      searchResultShows searchResult = new Gson().fromJson(response, searchResultShows.class);
   }
}
