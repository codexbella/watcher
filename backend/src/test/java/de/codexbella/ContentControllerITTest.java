package de.codexbella;

import de.codexbella.search.ShowSearchData;
import de.codexbella.user.LoginData;
import de.codexbella.user.RegisterData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContentControllerITTest {
   @Autowired
   private TestRestTemplate restTemplate;

   @MockBean
   private RestTemplate mockTemplate;

   private final String searchResult = "{\"page\":1,\"results\":[{\"backdrop_path\":\"/suopoADq0k8YZr4dQXcU6pToj6s.jpg\"," +
         "\"first_air_date\":\"2011-04-17\",\"genre_ids\":[10765,18,10759],\"id\":1399,\"name\":\"Game of Thrones\"," +
         "\"origin_country\":[\"US\"],\"original_language\":\"en\",\"original_name\":\"Game of Thrones\"," +
         "\"overview\":\"Seven noble families fight for control of the mythical land of Westeros. Friction between " +
         "the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the" +
         " war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of " +
         "men and icy horrors beyond.\",\"popularity\":587.684,\"poster_path\":\"/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg\"," +
         "\"vote_average\":8.4,\"vote_count\":17570},{\"backdrop_path\":null,\"first_air_date\":\"2020-04-14\"," +
         "\"genre_ids\":[],\"id\":138757,\"name\":\"Autópsia Game Of Thrones\",\"origin_country\":[]," +
         "\"original_language\":\"pt\",\"original_name\":\"Autópsia Game Of Thrones\",\"overview\":\"\"," +
         "\"popularity\":1.656,\"poster_path\":null,\"vote_average\":0,\"vote_count\":0}],\"total_pages\":1," +
         "\"total_results\":2}";

   @Test
   void integrationTest(@Value("${app.api.key}") String apiKey) {
      // should register a new user
      RegisterData registerDataUser1 = new RegisterData();
      registerDataUser1.setUsername("whoever");
      registerDataUser1.setPassword("very-safe-password");
      registerDataUser1.setPasswordAgain("very-safe-password");

      ResponseEntity<String> responseRegister = restTemplate.postForEntity("/api/users/register", registerDataUser1, String.class);

      assertThat(responseRegister.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertEquals("New user created with username " + registerDataUser1.getUsername(), responseRegister.getBody());

      // should not register new user because passwords do not match
      RegisterData registerDataUser2 = new RegisterData();
      registerDataUser2.setUsername(registerDataUser1.getUsername());
      registerDataUser2.setPassword("tadada");
      registerDataUser2.setPasswordAgain("tududu");

      ResponseEntity<String> responseNotRegister1 = restTemplate.postForEntity("/api/users/register", registerDataUser2,
            String.class);

      assertThat(responseNotRegister1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertEquals("Passwords mismatched.", responseNotRegister1.getBody());

      // should not register new user because username already in use
      registerDataUser2.setPasswordAgain("tadada");

      ResponseEntity<String> responseNotRegister2 = restTemplate.postForEntity("/api/users/register", registerDataUser2,
            String.class);

      assertThat(responseNotRegister2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertEquals("Username " + registerDataUser2.getUsername() + " already in use.", responseNotRegister2.getBody());

      // should log in user
      LoginData user1 = new LoginData();
      user1.setUsername("whoever");
      user1.setPassword("very-safe-password");

      ResponseEntity<String> responseLoginUser1 = restTemplate.postForEntity("/api/users/login", user1, String.class);

      System.out.println("testing print out should log in user");

      assertThat(responseLoginUser1.getStatusCode()).isEqualTo(HttpStatus.OK);

      // should not log in user
      LoginData user2 = new LoginData();
      user2.setUsername(registerDataUser1.getUsername());
      user2.setPassword("xxx");

      ResponseEntity<String> responseNoLogin = restTemplate.postForEntity("/api/users/login", user2, String.class);

      assertThat(responseNoLogin.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

      // should search api
      HttpHeaders headerForUser1 = new HttpHeaders();
      headerForUser1.set("Authorization", "Bearer" + responseLoginUser1.getBody());
      HttpEntity<ShowSearchData> httpEntityUser1Get = new HttpEntity<>(headerForUser1);

      Mockito.when(mockTemplate.getForObject("https://api.themoviedb.org/3/search/tv?api_key="+apiKey
                  +"&query=game+of+thrones", String.class))
            .thenReturn(searchResult);

      ResponseEntity<ShowSearchData[]> responseSearch = restTemplate.exchange("/api/search/game+of+thrones",
            HttpMethod.GET, httpEntityUser1Get, ShowSearchData[].class);
      assertThat(responseSearch.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSearch.getBody()).isNotNull();
      ShowSearchData[] arraySearch = responseSearch.getBody();

      assertThat(arraySearch[0].getApiId()).isEqualTo(1399);
      assertThat(arraySearch[0].getName()).isEqualTo("Game of Thrones");
      assertThat(arraySearch[1].getApiId()).isEqualTo(138757);
      assertThat(arraySearch[1].getName()).isEqualTo("Autópsia Game Of Thrones");
   }
}
