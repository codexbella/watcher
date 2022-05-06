package de.codexbella;

import com.google.gson.Gson;
import de.codexbella.content.ContentMapper;
import de.codexbella.content.Seen;
import de.codexbella.content.Show;
import de.codexbella.content.ShowApi;
import de.codexbella.content.episode.Episode;
import de.codexbella.content.season.Season;
import de.codexbella.content.season.SeasonApi;
import de.codexbella.search.SearchResultShows;
import de.codexbella.search.ShowSearchData;
import de.codexbella.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ContentService {
   private final RestTemplate restTemplate;
   private final String apiKey;
   private final ShowRepository showRepository;
   private final ContentMapper contentMapper;
   private final UserRepository userRepository;

   public ContentService(@Value("${app.api.key}") String apiKey, RestTemplate restTemplate,
                         ShowRepository showRepository, ContentMapper contentMapper, UserRepository userRepository) {
      this.restTemplate = restTemplate;
      this.apiKey = apiKey;
      this.showRepository = showRepository;
      this.contentMapper = contentMapper;
      this.userRepository = userRepository;
   }

   public List<ShowSearchData> searchForShows(String searchTerm, String username) throws InvalidParameterException {
      String language = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
            () -> new InvalidParameterException("user with "+username+" unknown.")).getLanguage();
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

   public void saveShow(int showApiId, String username) throws IllegalArgumentException {
      String language = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
            () -> new InvalidParameterException("user with "+username+" unknown.")).getLanguage();
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

   public List<Show> getMatchingShows(String searchterm, String username) {
      return showRepository.findByNameContainsIgnoreCaseAndUsername(searchterm, username);
   }

   public Optional<Show> getShow(int showApiId, String username) {
      return showRepository.findByApiIdAndUsername(showApiId, username);
   }

   public Optional<Show> saveSeason(int showApiId, int seasonNumber, String username) {
      String language = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
            () -> new InvalidParameterException("user with "+username+" unknown.")).getLanguage();
      Optional<Show> showOptional = showRepository.findByApiIdAndUsername(showApiId, username);
      if (showOptional.isPresent()) {
         Season seasonBefore = showOptional.get().getSeasons().get(seasonNumber-1);
         String response = restTemplate.getForObject(
               "https://api.themoviedb.org/3/tv/"+showApiId+"/season/"+seasonNumber+"?api_key="+apiKey
                     +"&language="+language, String.class);
         SeasonApi seasonApi = new Gson().fromJson(response, SeasonApi.class);
         Season season = contentMapper.toSeason(seasonApi, username);
         season.setNumberOfEpisodes(seasonBefore.getNumberOfEpisodes());
         season.setSeen(seasonBefore.getSeen());
         if (seasonBefore.getSeen() == Seen.YES) {
            for (Episode episode : season.getEpisodes()) {
               episode.setSeen(Seen.YES);
            }
         }
         season.setRating(seasonBefore.getRating());
         showOptional.get().getSeasons().set(seasonNumber-1, season);
         showRepository.save(showOptional.get());
      }
      return showOptional;
   }

   public Optional<Show> editShow(String showId, Integer rating, Seen seen, Integer seasonNumber, Integer episodeNumber, String username) {
      Optional<Show> showOptional = showRepository.findByIdAndUsername(showId, username);
      if (showOptional.isPresent()) {
         Show show = showOptional.get();
         if (rating != null) {
            if (episodeNumber != null) {
               show.getSeasons().get(seasonNumber-1).getEpisodes().get(episodeNumber-1).setRating(rating);
            } else if (seasonNumber != null) {
               show.getSeasons().get(seasonNumber-1).setRating(rating);
            } else {
               show.setRating(rating);
            }
         }

         if (seen != null) {
            if (episodeNumber != null) {
               Season season = show.getSeasons().get(seasonNumber-1);
               show.getSeasons().get(seasonNumber-1).getEpisodes().get(episodeNumber-1).setSeen(seen);
               if (seen == Seen.NO) {
                  if (season.getEpisodes().stream().allMatch(e -> e.getSeen() == Seen.NO)) {
                     season.setSeen(Seen.NO);
                  } else {
                     season.setSeen(Seen.PARTIAL);
                  }
                  if (show.getSeasons().stream().allMatch(s -> s.getSeen() == Seen.NO)) {
                     show.setSeen(Seen.NO);
                  } else {
                     show.setSeen(Seen.PARTIAL);
                  }
               } else if (seen == Seen.YES) {
                  if (season.getEpisodes().stream().allMatch(e -> e.getSeen() == Seen.YES)) {
                     season.setSeen(Seen.YES);
                     if (show.getSeasons().stream().allMatch(s -> s.getSeen() == Seen.YES)) {
                        show.setSeen(Seen.YES);
                     } else {
                        show.setSeen(Seen.PARTIAL);
                     }
                  } else {
                     season.setSeen(Seen.PARTIAL);
                     show.setSeen(Seen.PARTIAL);
                  }
               }
            } else if (seasonNumber != null) {
               show.getSeasons().get(seasonNumber-1).setSeen(seen);
               if (seen == Seen.YES) {
                  for (Episode episode : show.getSeasons().get(seasonNumber-1).getEpisodes()) {
                     episode.setSeen(Seen.YES);
                  }
                  if (show.getSeasons().stream().allMatch(s -> s.getSeen() == Seen.YES)) {
                     show.setSeen(Seen.YES);
                  } else {
                     show.setSeen(Seen.PARTIAL);
                  }
               } else if (seen == Seen.NO) {
                  for (Episode episode : show.getSeasons().get(seasonNumber-1).getEpisodes()) {
                     episode.setSeen(Seen.NO);
                  }
                  if (show.getSeasons().stream().allMatch(s -> s.getSeen() == Seen.NO)) {
                     show.setSeen(Seen.NO);
                  } else {
                     show.setSeen(Seen.PARTIAL);
                  }
               }
            } else {
               show.setSeen(seen);
               if (seen == Seen.YES || seen == Seen.NO) {
                  for (Season season : show.getSeasons()) {
                     season.setSeen(seen);
                     for (Episode episode : season.getEpisodes()) {
                        episode.setSeen(seen);
                     }
                  }
               }
            }
         }
         return Optional.of(showRepository.save(show));
      }
      return Optional.empty();
   }
}
