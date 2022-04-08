package de.codexbella;

import com.google.gson.Gson;
import de.codexbella.content.ContentMapper;
import de.codexbella.content.Show;
import de.codexbella.content.ShowApi;
import de.codexbella.search.SearchResultShows;
import de.codexbella.search.ShowSearchData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ContentService {
   private final RestTemplate restTemplate;
   private final String apiKey;
   private final ShowRepository showRepository;
   private final ContentMapper contentMapper;

   public ContentService(@Value("${app.api.key}") String apiKey, RestTemplate restTemplate,
                         ShowRepository showRepository, ContentMapper contentMapper) {
      this.restTemplate = restTemplate;
      this.apiKey = apiKey;
      this.showRepository = showRepository;
      this.contentMapper = contentMapper;
   }

   public List<ShowSearchData> searchForShows(String language, String searchTerm) {
      String response = restTemplate.getForObject(
            "https://api.themoviedb.org/3/search/tv?api_key="+apiKey+"&language="+language+"&query="
                  +searchTerm, String.class);
      SearchResultShows results = new Gson().fromJson(response, SearchResultShows.class);
      Stream<ShowSearchData> resultListStream = results.getShows().stream();
      if (results.getNumberOfPages()>1) {
         for (int i = 2; i <= results.getNumberOfPages(); i++) {
            String toAdd = restTemplate.getForObject(
                  "https://api.themoviedb.org/3/search/tv?api_key="+apiKey+"&language="+language+"&query="
                        +searchTerm+"&page="+i, String.class);
            SearchResultShows resultsAdditional = new Gson().fromJson(toAdd, SearchResultShows.class);
            Stream<ShowSearchData> resultListAdditionalStream = resultsAdditional.getShows().stream();
            resultListStream = Stream.concat(resultListStream, resultListAdditionalStream);
         }
      }
      return resultListStream.distinct().toList();
   }

   public ShowApi saveShow(String language, int apiId, String username) {
      String response = restTemplate.getForObject(
            "https://api.themoviedb.org/3/tv/"+apiId+"?api_key="+apiKey+"&language="+language, String.class);
      ShowApi showApi = new Gson().fromJson(response, ShowApi.class);
      Show show = contentMapper.toShow(showApi);
      show.setUser(username);
      for (int i = 0; i < show.getSeasons().size(); i++) {
         show.getSeasons().get(i).setUser(username);
         for (int j = 0; j < show.getSeasons().get(i).getEpisodes().size(); j++) {
            show.getSeasons().get(i).getEpisodes().get(j).setUser(username);
         }
      }
      showRepository.save(show);
      return showApi;
   }
}
