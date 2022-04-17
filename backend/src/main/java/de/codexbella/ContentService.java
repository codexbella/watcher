package de.codexbella;

import com.google.gson.Gson;
import de.codexbella.content.ContentMapper;
import de.codexbella.content.Show;
import de.codexbella.content.ShowApi;
import de.codexbella.content.season.Season;
import de.codexbella.content.season.SeasonApi;
import de.codexbella.search.SearchResultShows;
import de.codexbella.search.ShowSearchData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
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

   public List<ShowSearchData> searchForShows(String language, String searchTerm, String username) {
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
      List<ShowSearchData> result = resultListStream.distinct().toList();
      for (ShowSearchData searchResult : result) {
         if (showRepository.findByApiIdAndUsername(searchResult.getApiId(), username).isPresent()) {
            searchResult.setLiked(true);
         }
      }
      return result;
   }

   public void saveShow(String language, int showApiId, String username) throws IllegalArgumentException {
      Optional<Show> showOptional = showRepository.findByApiIdAndUsername(showApiId, username);
      if (showOptional.isEmpty()) {
         String response = restTemplate.getForObject(
               "https://api.themoviedb.org/3/tv/" + showApiId + "?api_key=" + apiKey + "&language=" + language, String.class);
         ShowApi showApi = new Gson().fromJson(response, ShowApi.class);
         Show show = contentMapper.toShow(showApi, username);
         showRepository.save(show);
      } else {
         throw new IllegalArgumentException("Show "+showOptional.get().getName()+" with api id "+showApiId+" already saved");
      }
   }

   public void deleteShow(int showApiId, String username) {
      Optional<Show> showOptional = showRepository.findByApiIdAndUsername(showApiId, username);
      showOptional.ifPresent(show -> showRepository.deleteById(show.getId()));
   }

   public List<Show> getAllShows(String username) {
      return showRepository.findAllByUsername(username);
   }

   public Optional<Show> getShow(String showId, String username) {
      return showRepository.findByIdAndUsername(showId, username);
   }

   public Optional<Show> getSeason(String language, int showApiId, int seasonNumber, String username) {
      Optional<Show> showOptional = showRepository.findByApiIdAndUsername(showApiId, username);
      if (showOptional.isPresent()) {
         String response = restTemplate.getForObject(
               "https://api.themoviedb.org/3/tv/"+showApiId+"/season/"+seasonNumber+"?api_key="+apiKey
                     +"&language="+language, String.class);
         SeasonApi seasonApi = new Gson().fromJson(response, SeasonApi.class);
         Season season = contentMapper.toSeason(seasonApi, username);
         season.setNumberOfEpisodes(showOptional.get().getSeasons().get(seasonNumber-1).getNumberOfEpisodes());
         showOptional.get().getSeasons().set(seasonNumber-1, season);
         showRepository.save(showOptional.get());
      }
      return showOptional;
   }
}
