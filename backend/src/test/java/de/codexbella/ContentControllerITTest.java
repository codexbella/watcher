package de.codexbella;

import de.codexbella.search.ShowSearchData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContentControllerITTest {
   @Autowired
   private TestRestTemplate restTemplate;

   @MockBean
   private RestTemplate mockTemplate;

   private String searchResult = "{\"page\":1,\"results\":[{\"backdrop_path\":\"/suopoADq0k8YZr4dQXcU6pToj6s.jpg\"," +
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
   void integrationTest() {
      // should search api
      Mockito.when(mockTemplate.getForObject("https://api.themoviedb.org/3/search/tv?api_key=do-not-tell&query=game+of+thrones", String.class))
            .thenReturn(searchResult);
      String searchTerm = "game+of+thrones";

      ResponseEntity<ShowSearchData[]> responseSearch =
            restTemplate.getForEntity("/api/watcher/search/" + searchTerm, ShowSearchData[].class);
      assertThat(responseSearch.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseSearch.getBody()).isNotNull();
      ShowSearchData[] arraySearch = responseSearch.getBody();

      assertThat(arraySearch[0].getApiId()).isEqualTo(1399);
      assertThat(arraySearch[0].getName()).isEqualTo("Game of Thrones");
      assertThat(arraySearch[1].getApiId()).isEqualTo(138757);
      assertThat(arraySearch[1].getName()).isEqualTo("Autópsia Game Of Thrones");
   }
}