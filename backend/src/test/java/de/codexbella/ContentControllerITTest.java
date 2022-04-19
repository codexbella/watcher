package de.codexbella;

import de.codexbella.content.Show;
import de.codexbella.content.season.Season;
import de.codexbella.search.ShowSearchData;
import de.codexbella.user.LoginData;
import de.codexbella.user.RegisterData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContentControllerITTest {
   @Autowired
   private TestRestTemplate restTemplate;

   @MockBean
   private RestTemplate mockTemplate;

   @Test
   void integrationTest(@Value("${app.api.key}") String apiKey) throws IOException {
      // should register a new user
      RegisterData registerDataUser1 = new RegisterData();
      registerDataUser1.setUsername("whoever");
      registerDataUser1.setPassword("very-safe-password");
      registerDataUser1.setPasswordAgain("very-safe-password");

      ResponseEntity<String> responseRegister = restTemplate.postForEntity("/api/users/register", registerDataUser1,
            String.class);

      assertThat(responseRegister.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertEquals("New user created with username " + registerDataUser1.getUsername(),
            responseRegister.getBody());

      // should not register new user because passwords do not match
      RegisterData registerDataUser2 = new RegisterData();
      registerDataUser2.setUsername(registerDataUser1.getUsername());
      registerDataUser2.setPassword("tadada");
      registerDataUser2.setPasswordAgain("tududu");

      ResponseEntity<String> responseNotRegister1 = restTemplate.postForEntity("/api/users/register",
            registerDataUser2, String.class);

      assertThat(responseNotRegister1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertEquals("Passwords mismatched", responseNotRegister1.getBody());

      // should not register new user because username already in use
      registerDataUser2.setPasswordAgain("tadada");

      ResponseEntity<String> responseNotRegister2 = restTemplate.postForEntity("/api/users/register",
            registerDataUser2, String.class);

      assertThat(responseNotRegister2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertEquals("Username " + registerDataUser2.getUsername() + " already in use",
            responseNotRegister2.getBody());

      // should log in user
      LoginData user1 = new LoginData();
      user1.setUsername("whoever");
      user1.setPassword("very-safe-password");

      ResponseEntity<String> responseLoginUser1 = restTemplate.postForEntity("/api/users/login", user1,
            String.class);

      assertThat(responseLoginUser1.getStatusCode()).isEqualTo(HttpStatus.OK);

      // should not log in user
      LoginData user2 = new LoginData();
      user2.setUsername(registerDataUser1.getUsername());
      user2.setPassword("xxx");

      ResponseEntity<String> responseNoLogin = restTemplate.postForEntity("/api/users/login", user2, String.class);

      assertThat(responseNoLogin.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

      // should search api
      HttpHeaders headerForUser1 = new HttpHeaders();
      headerForUser1.set("Authorization", "Bearer" + responseLoginUser1.getBody());
      HttpEntity<ShowSearchData> httpEntityUser1Get = new HttpEntity<>(headerForUser1);

      when(mockTemplate.getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
                  +"&language=en-US&query=game+of+thrones", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultOnePage.txt")));

      ResponseEntity<ShowSearchData[]> responseSearch = restTemplate.exchange(
            "/api/search/game+of+thrones?language=en-US", HttpMethod.GET, httpEntityUser1Get,
            ShowSearchData[].class);
      assertThat(responseSearch.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSearch.getBody()).isNotNull();
      ShowSearchData[] arraySearch = responseSearch.getBody();

      assertThat(arraySearch[0].getApiId()).isEqualTo(1399);
      assertThat(arraySearch[0].getName()).isEqualTo("Game of Thrones");
      assertThat(arraySearch[1].getApiId()).isEqualTo(138757);
      assertThat(arraySearch[1].getName()).isEqualTo("Autópsia Game Of Thrones");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
            +"&language=en-US&query=game+of+thrones", String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should search api with two page result
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
            +"&language=en-US&query=voyager", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultTwoPageFirstPage.txt")));
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
            +"&language=en-US&query=voyager&page=2", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultTwoPageSecondPage.txt")));

      ResponseEntity<ShowSearchData[]> responseSearchTwoPage = restTemplate.exchange(
            "/api/search/voyager?language=en-US", HttpMethod.GET, httpEntityUser1Get, ShowSearchData[].class);
      assertThat(responseSearchTwoPage.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSearchTwoPage.getBody()).isNotNull();
      ShowSearchData[] arraySearchTwoPage = responseSearchTwoPage.getBody();

      assertThat(arraySearchTwoPage.length).isEqualTo(25);
      assertThat(arraySearchTwoPage[0].getApiId()).isEqualTo(1855);
      assertThat(arraySearchTwoPage[0].getName()).isEqualTo("Star Trek: Voyager");
      assertThat(arraySearchTwoPage[1].getApiId()).isEqualTo(76931);
      assertThat(arraySearchTwoPage[1].getName()).isEqualTo("Voyager");

      assertThat(arraySearchTwoPage[20].getApiId()).isEqualTo(39976);
      assertThat(arraySearchTwoPage[20].getName()).isEqualTo("The Voyages of Young Doctor Dolittle");
      assertThat(arraySearchTwoPage[21].getApiId()).isEqualTo(12152);
      assertThat(arraySearchTwoPage[21].getName()).isEqualTo("This Is America, Charlie Brown");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
            +"&language=en-US&query=voyager", String.class);
      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
            +"&language=en-US&query=voyager&page=2", String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should save show
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/tv/1855?api_key="+apiKey+"&language=en-US",
            String.class)).thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de",
            "codexbella", "data", "searchResultVoyager.txt")));

      ResponseEntity<String> responseSavedShow = restTemplate.exchange("/api/saveshow/1855?language=en-US",
            HttpMethod.PUT, httpEntityUser1Get, String.class);
      assertThat(responseSavedShow.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSavedShow.getBody()).isNotNull();
      String creationMessage = responseSavedShow.getBody();

      assertThat(creationMessage).isEqualTo("Show saved");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/tv/1855?api_key="+apiKey+"&language=en-US",
            String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should not save show because already in database
      ResponseEntity<String> responseNotSavedShow = restTemplate.exchange("/api/saveshow/1855?language=en-US",
            HttpMethod.PUT, httpEntityUser1Get, String.class);
      assertThat(responseNotSavedShow.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertThat(responseNotSavedShow.getBody()).isNotNull();
      String showAlreadyExistsMessage = responseNotSavedShow.getBody();

      assertThat(showAlreadyExistsMessage).isEqualTo("Show Star Trek: Voyager with api id 1855 already saved");

      verifyNoMoreInteractions(mockTemplate);

      // should get all shows for user
      ResponseEntity<Show[]> responseAllShows = restTemplate.exchange("/api/getallshows",
            HttpMethod.GET, httpEntityUser1Get, Show[].class);
      assertThat(responseAllShows.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseAllShows.getBody()).isNotNull();
      Show[] arrayShows = responseAllShows.getBody();

      assertThat(arrayShows[0].getApiId()).isEqualTo(1855);

      // should get show details
      ResponseEntity<Show> responseShow = restTemplate.exchange("/api/getshow/"+arrayShows[0].getId(),
            HttpMethod.GET, httpEntityUser1Get, Show.class);
      assertThat(responseShow.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseShow.getBody()).isNotNull();
      Show show = responseShow.getBody();

      assertThat(show.getApiId()).isEqualTo(1855);

      // should save season
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/tv/1855/season/1?api_key="+apiKey
            +"&language=en-US", String.class)).thenReturn(Files.readString(Path.of(".", "src",
            "test", "java", "de", "codexbella", "data", "resultVoyagerSeason1.txt")));

      ResponseEntity<Show> responseGetSeason = restTemplate.exchange("/api/saveseason/1855?seasonNumber=1",
            HttpMethod.PUT, httpEntityUser1Get, Show.class);
      assertThat(responseGetSeason.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseGetSeason.getBody()).isNotNull();
      List<Season> seasons = responseGetSeason.getBody().getSeasons();

      assertThat(seasons.get(0).getEpisodes().get(0).getName()).isEqualTo("Caretaker (1)");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/tv/1855/season/1?api_key="+apiKey
            +"&language=en-US", String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should change rating of show
      ResponseEntity<Show> responseEditShow = restTemplate.exchange("/api/editshow/"+arrayShows[0].getId()+"?rating=4",
            HttpMethod.PUT, httpEntityUser1Get, Show.class);
      assertThat(responseEditShow.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseEditShow.getBody()).isNotNull();

      Show showWithChangedRating = responseEditShow.getBody();

      assertThat(showWithChangedRating.getApiId()).isEqualTo(1855);
      assertThat(showWithChangedRating.getRating()).isEqualTo(4);

      // should delete show
      restTemplate.exchange("/api/deleteshow/1855", HttpMethod.DELETE, httpEntityUser1Get, Void.class);

      ResponseEntity<Show[]> responseAllShowsAfterDelete = restTemplate.exchange("/api/getallshows",
            HttpMethod.GET, httpEntityUser1Get, Show[].class);
      assertThat(responseAllShowsAfterDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseAllShowsAfterDelete.getBody()).isNotNull();
      Show[] arrayShowsAfterDelete = responseAllShowsAfterDelete.getBody();

      assertThat(arrayShowsAfterDelete.length).isEqualTo(0);

      // should not get season because show not saved anymore
      ResponseEntity<Show> responseNotFound = restTemplate.exchange("/api/saveseason/1855?seasonNumber=1",
            HttpMethod.PUT, httpEntityUser1Get, Show.class);
      assertThat(responseNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(responseNotFound.getBody()).isEqualTo(null);
      verifyNoMoreInteractions(mockTemplate);
   }
}
