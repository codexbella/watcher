package de.codexbella;

import de.codexbella.content.Seen;
import de.codexbella.content.Show;
import de.codexbella.content.season.Season;
import de.codexbella.search.ShowSearchData;
import de.codexbella.user.LoginData;
import de.codexbella.user.LoginResponseBody;
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
      registerDataUser1.setLanguage("en-US");

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
      registerDataUser1.setLanguage("en-US");

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

      ResponseEntity<LoginResponseBody> responseLoginUser1 = restTemplate.postForEntity("/api/users/login", user1,
            LoginResponseBody.class);

      assertThat(responseLoginUser1.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseLoginUser1.getBody()).isNotNull();

      // should not log in user
      LoginData user2 = new LoginData();
      user2.setUsername(registerDataUser1.getUsername());
      user2.setPassword("xxx");

      ResponseEntity<LoginResponseBody> responseNoLogin = restTemplate.postForEntity("/api/users/login", user2,
            LoginResponseBody.class);

      assertThat(responseNoLogin.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

      // should search api
      HttpHeaders headerForUser1 = new HttpHeaders();
      headerForUser1.setBearerAuth(responseLoginUser1.getBody().getToken());
      HttpEntity<ShowSearchData> httpEntityUser1Get = new HttpEntity<>(headerForUser1);

      when(mockTemplate.getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
                  +"&language=en-US&query=game+of+thrones", String.class))
            .thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de", "codexbella", "data",
                  "searchResultOnePage.txt")));

      ResponseEntity<ShowSearchData[]> responseSearch = restTemplate.exchange(
            "/api/search/game+of+thrones", HttpMethod.GET, httpEntityUser1Get,
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
            "/api/search/voyager", HttpMethod.GET, httpEntityUser1Get, ShowSearchData[].class);
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
            "codexbella", "data", "resultVoyager.txt")));

      ResponseEntity<String> responseSavedShow = restTemplate.exchange("/api/saveshow/1855",
            HttpMethod.PUT, httpEntityUser1Get, String.class);
      assertThat(responseSavedShow.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSavedShow.getBody()).isNotNull();
      String creationMessage = responseSavedShow.getBody();

      assertThat(creationMessage).isEqualTo("Show saved");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/tv/1855?api_key="+apiKey+"&language=en-US",
            String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should not save show because already in database
      ResponseEntity<String> responseNotSavedShow = restTemplate.exchange("/api/saveshow/1855",
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
      ResponseEntity<Show> responseEditShowRating = restTemplate.exchange(
            "/api/editshow/"+arrayShows[0].getId()+"?rating=4", HttpMethod.PUT, httpEntityUser1Get, Show.class);
      assertThat(responseEditShowRating.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseEditShowRating.getBody()).isNotNull();

      Show showWithChangedRating = responseEditShowRating.getBody();

      assertThat(showWithChangedRating.getApiId()).isEqualTo(1855);
      assertThat(showWithChangedRating.getRating()).isEqualTo(4);

      // should change seen status of show
      ResponseEntity<Show> responseEditShowSeenStatus = restTemplate.exchange(
            "/api/editshow/"+arrayShows[0].getId()+"?seen="+ Seen.PARTIAL, HttpMethod.PUT, httpEntityUser1Get, Show.class);
      assertThat(responseEditShowSeenStatus.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseEditShowSeenStatus.getBody()).isNotNull();

      Show showWithChangedSeenStatus = responseEditShowSeenStatus.getBody();

      assertThat(showWithChangedSeenStatus.getApiId()).isEqualTo(1855);
      assertThat(showWithChangedSeenStatus.getSeen()).isEqualTo(Seen.PARTIAL);

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

      // should save show Star Trek Picard, apiId 85949
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/tv/85949?api_key="+apiKey+"&language=en-US",
          String.class)).thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de",
          "codexbella", "data", "resultPicard.txt")));

      ResponseEntity<String> responseSavedShowPicard = restTemplate.exchange("/api/saveshow/85949",
          HttpMethod.PUT, httpEntityUser1Get, String.class);
      assertThat(responseSavedShowPicard.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSavedShowPicard.getBody()).isNotNull();
      String creationMessagePicard = responseSavedShowPicard.getBody();

      assertThat(creationMessagePicard).isEqualTo("Show saved");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/tv/85949?api_key="+apiKey+"&language=en-US",
          String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should save show The Good Place, apiId 66573
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/tv/66573?api_key="+apiKey+"&language=en-US",
          String.class)).thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de",
          "codexbella", "data", "resultTheGoodPlace.txt")));

      ResponseEntity<String> responseSavedShowTheGoodPlace = restTemplate.exchange("/api/saveshow/66573",
          HttpMethod.PUT, httpEntityUser1Get, String.class);
      assertThat(responseSavedShowTheGoodPlace.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSavedShowTheGoodPlace.getBody()).isNotNull();

      assertThat(responseSavedShowTheGoodPlace.getBody()).isEqualTo("Show saved");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/tv/66573?api_key="+apiKey+"&language=en-US",
          String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should save show The Good Fight, apiId 69158
      when(mockTemplate.getForObject("https://api.themoviedb.org/3/tv/69158?api_key="+apiKey+"&language=en-US",
          String.class)).thenReturn(Files.readString(Path.of(".", "src", "test", "java", "de",
          "codexbella", "data", "resultTheGoodFight.txt")));

      ResponseEntity<String> responseSavedShowTheFightPlace = restTemplate.exchange("/api/saveshow/69158",
          HttpMethod.PUT, httpEntityUser1Get, String.class);
      assertThat(responseSavedShowTheFightPlace.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSavedShowTheFightPlace.getBody()).isNotNull();

      assertThat(responseSavedShowTheFightPlace.getBody()).isEqualTo("Show saved");

      verify(mockTemplate).getForObject("https://api.themoviedb.org/3/tv/69158?api_key="+apiKey+"&language=en-US",
          String.class);
      verifyNoMoreInteractions(mockTemplate);

      // should get all shows for user
      ResponseEntity<Show[]> responseAllShows2 = restTemplate.exchange("/api/getallshows",
          HttpMethod.GET, httpEntityUser1Get, Show[].class);
      assertThat(responseAllShows2.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseAllShows2.getBody()).isNotNull();
      Show[] arrayShows2 = responseAllShows2.getBody();

      assertThat(arrayShows2.length).isEqualTo(3);
      assertThat(arrayShows2[0].getApiId()).isEqualTo(85949);
      assertThat(arrayShows2[1].getApiId()).isEqualTo(66573);
      assertThat(arrayShows2[2].getApiId()).isEqualTo(69158);

      // should get shows matching "good"
      ResponseEntity<Show[]> responseMatchingGood = restTemplate.exchange("/api/getmatchingshows?searchterm=good",
          HttpMethod.GET, httpEntityUser1Get, Show[].class);
      assertThat(responseMatchingGood.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseMatchingGood.getBody()).isNotNull();
      Show[] arrayShowsMatchingGood = responseMatchingGood.getBody();

      assertThat(arrayShowsMatchingGood.length).isEqualTo(2);
      assertThat(arrayShowsMatchingGood[0].getApiId()).isEqualTo(66573);
      assertThat(arrayShowsMatchingGood[1].getApiId()).isEqualTo(69158);
   }
}
