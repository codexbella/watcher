package de.codexbella;

import de.codexbella.content.ContentMapper;
import de.codexbella.content.Seen;
import de.codexbella.content.Show;
import de.codexbella.content.season.Season;
import de.codexbella.search.ShowSearchData;
import de.codexbella.user.UserData;
import de.codexbella.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;

class ContentServiceTest {

   @Test
   void shouldSearchMockApiForShowWithOnePageResult() throws IOException {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      UserData userData = new UserData();
      userData.setUsername("testuser");
      userData.setLanguage("en-US");
      when(mockUserRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(userData));
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);
      String searchTerm = "game+of+thrones";
      when(mockApi.getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm, String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultOnePage.txt")));

      List<ShowSearchData> searchResult = contentService.searchForShows(searchTerm, "testuser");

      assertThat(searchResult.size()).isEqualTo(2);
      assertThat(searchResult.get(0).getApiId()).isEqualTo(1399);
      assertThat(searchResult.get(0).getName()).isEqualTo("Game of Thrones");
      assertThat(searchResult.get(1).getApiId()).isEqualTo(138757);
      assertThat(searchResult.get(1).getName()).isEqualTo("Autópsia Game Of Thrones");
   }
   @Test
   void shouldSearchMockApiForShowWithTwoPageResult() throws IOException {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      UserData userData = new UserData();
      userData.setUsername("testuser");
      userData.setLanguage("en-US");
      when(mockUserRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(userData));
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);
      String searchTerm = "voyager";
      when(mockApi.getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm, String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultTwoPageFirstPage.txt")));
      when(mockApi.getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm+"&page=2", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultTwoPageSecondPage.txt")));

      List<ShowSearchData> searchResult = contentService.searchForShows(searchTerm, "testuser");

      assertThat(searchResult.size()).isEqualTo(25);
      assertThat(searchResult.get(0).getApiId()).isEqualTo(1855);
      assertThat(searchResult.get(0).getName()).isEqualTo("Star Trek: Voyager");
      assertThat(searchResult.get(1).getApiId()).isEqualTo(76931);
      assertThat(searchResult.get(1).getName()).isEqualTo("Voyager");

      assertThat(searchResult.get(20).getApiId()).isEqualTo(39976);
      assertThat(searchResult.get(20).getName()).isEqualTo("The Voyages of Young Doctor Dolittle");
      assertThat(searchResult.get(21).getApiId()).isEqualTo(12152);
      assertThat(searchResult.get(21).getName()).isEqualTo("This Is America, Charlie Brown");

      verify(mockApi).getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm, String.class);
      verify(mockApi).getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm+"&page=2", String.class);
      verifyNoMoreInteractions(mockApi);
   }
   @Test
   void shouldSaveShow() throws IllegalArgumentException, IOException {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      UserData userData = new UserData();
      userData.setUsername("testuser");
      userData.setLanguage("en-US");
      when(mockUserRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(userData));
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);
      when(mockApi.getForObject("https://api.themoviedb.org/3/tv/1855?api_key=xxx&language=en-US", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "resultVoyager.txt")));

      contentService.saveShow(1855, "testuser");

      verify(mockApi).getForObject("https://api.themoviedb.org/3/tv/1855?api_key=xxx&language=en-US", String.class);
      verifyNoMoreInteractions(mockApi);

      verify(mockShowRepo).findByApiIdAndUsername(1855, "testuser");
      verify(mockShowRepo).save(any());
      verifyNoMoreInteractions(mockShowRepo);
   }
   @Test
   void shouldNotSaveShow() throws IOException {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      when(mockShowRepo.findByApiIdAndUsername(1855, "testuser")).thenReturn(Optional.of(new Show()));
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      UserData userData = new UserData();
      userData.setUsername("testuser");
      userData.setLanguage("en-US");
      when(mockUserRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(userData));
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);
      when(mockApi.getForObject("https://api.themoviedb.org/3/tv/1855?api_key=xxx&language=en-US", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "resultVoyager.txt")));

      assertThatIllegalArgumentException().isThrownBy(() -> {
         contentService.saveShow(1855, "testuser");
      });

      verifyNoInteractions(mockApi);

      verify(mockShowRepo).findByApiIdAndUsername(1855, "testuser");
      verifyNoMoreInteractions(mockShowRepo);
   }
   @Test
   void shouldDeleteShow() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      Show voyager = new Show();
      voyager.setId("test-id");
      when(mockShowRepo.findByApiIdAndUsername(1855, "testuser")).thenReturn(Optional.of(voyager));
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      contentService.deleteShow(1855, "testuser");

      verify(mockShowRepo).findByApiIdAndUsername(1855, "testuser");
      verify(mockShowRepo).deleteById("test-id");
      verifyNoMoreInteractions(mockShowRepo);
   }
   @Test
   void shouldNotDeleteShow() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      when(mockShowRepo.findByApiIdAndUsername(1855, "testuser")).thenReturn(Optional.empty());
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      contentService.deleteShow(1855, "testuser");

      verify(mockShowRepo).findByApiIdAndUsername(1855, "testuser");
      verifyNoMoreInteractions(mockShowRepo);
   }
   @Test
   void shouldGetAllShowsForUser() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      Show show1 = new Show();
      show1.setId("test-id1");
      show1.setUsername("testuser");
      Show show2 = new Show();
      show2.setId("test-id2");
      show1.setUsername("testuser");
      when(mockShowRepo.findAllByUsername("testuser")).thenReturn(List.of(show1, show2));
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      assertThat(contentService.getAllShows("testuser")).isEqualTo(List.of(show1, show2));
   }
   @Test
   void shouldGetShowDetails() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      Show show1 = new Show();
      show1.setId("test-id1");
      show1.setUsername("testuser");
      show1.setName("test-show");
      when(mockShowRepo.findByIdAndUsername("test-id1", "testuser")).thenReturn(Optional.of(show1));
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      Optional<Show> showOptional = contentService.getShow("test-id1", "testuser");
      assertThat(showOptional).isPresent();
      assertThat(showOptional.get().getName()).isEqualTo("test-show");
   }
   @Test
   void shouldGetSeason() throws IOException {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      Show voyager = new Show();
      voyager.setId("test-id");
      voyager.setApiId(1855);
      voyager.setUsername("testuser");
      voyager.setName("Star Trek Voyager");
      Season season1 = new Season();
      season1.setApiId(5307);
      season1.setName("Season 1");
      List<Season> seasons = new ArrayList<>();
      seasons.add(season1);
      voyager.setSeasons(seasons);
      when(mockShowRepo.findByApiIdAndUsername(1855, "testuser")).thenReturn(Optional.of(voyager));
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      when(mockApi.getForObject("https://api.themoviedb.org/3/tv/1855/season/1?api_key=xxx&language=en-US",
            String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "resultVoyagerSeason1.txt")));
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      UserData userData = new UserData();
      userData.setUsername("testuser");
      userData.setLanguage("en-US");
      when(mockUserRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(userData));
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      Optional<Show> showOptional = contentService.saveSeason(1855, 1,
            "testuser");
      assertThat(showOptional).isPresent();
      assertThat(showOptional.get().getName()).isEqualTo("Star Trek Voyager");
      assertThat(showOptional.get().getSeasons().get(0).getEpisodes().get(0).getName()).isEqualTo("Caretaker (1)");
      assertThat(showOptional.get().getSeasons().get(0).getEpisodes().get(0).getUsername()).isEqualTo("testuser");

      verify(mockShowRepo).findByApiIdAndUsername(1855, "testuser");
   }
   @Test
   void shouldNotGetSeason(){
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      when(mockShowRepo.findByApiIdAndUsername(1855, "testuser")).thenReturn(Optional.empty());
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      UserData userData = new UserData();
      userData.setUsername("testuser");
      userData.setLanguage("en-US");
      when(mockUserRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(userData));
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      Optional<Show> showOptional = contentService.saveSeason(1855, 1,
            "testuser");
      assertThat(showOptional).isEmpty();
      verify(mockShowRepo).findByApiIdAndUsername(1855, "testuser");
      verifyNoMoreInteractions(mockShowRepo);
   }
   @Test
   void shouldEditShowRatingAndSeenStatus() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      Show show = new Show();
      show.setId("test-id");
      show.setUsername("testuser");
      show.setRating(2);
      when(mockShowRepo.findByIdAndUsername("test-id", "testuser")).thenReturn(Optional.of(show));
      when(mockShowRepo.save(show)).thenReturn(show);
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      Optional<Show> showOptionalRating = contentService.editShow("test-id",2, null,null,
            null, "testuser");

      assertThat(showOptionalRating).isPresent();
      assertThat(showOptionalRating.get().getId()).isEqualTo("test-id");
      assertThat(showOptionalRating.get().getRating()).isEqualTo(2);
      assertThat(showOptionalRating.get().getSeen()).isEqualTo(Seen.NO);
      verify(mockShowRepo).findByIdAndUsername("test-id", "testuser");
      verify(mockShowRepo).save(show);
      verifyNoMoreInteractions(mockShowRepo);

      show.setSeen(Seen.PARTIAL);
      Optional<Show> showOptionalSeen = contentService.editShow("test-id",null, Seen.PARTIAL,
            null, null, "testuser");

      assertThat(showOptionalSeen).isPresent();
      assertThat(showOptionalRating.get().getId()).isEqualTo("test-id");
      assertThat(showOptionalRating.get().getSeen()).isEqualTo(Seen.PARTIAL);
   }
   @Test
   void shouldGetMatchingShowsForUser() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      Show show1 = new Show();
      show1.setId("test-id1");
      show1.setUsername("testuser");
      Show show2 = new Show();
      show2.setId("test-id2");
      show1.setUsername("testuser");
      when(mockShowRepo.findByNameContainsIgnoreCaseAndUsername("id", "testuser")).thenReturn(List.of(show1, show2));
      UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper, mockUserRepo);

      assertThat(contentService.getMatchingShows("id", "testuser")).isEqualTo(List.of(show1, show2));
   }
}
