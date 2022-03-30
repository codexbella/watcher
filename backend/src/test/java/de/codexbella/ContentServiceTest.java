package de.codexbella;

import de.codexbella.search.SearchResultShows;
import de.codexbella.search.ShowSearchData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentServiceTest {
   @Autowired
   ContentService contentService;

   @Test
   void shouldSearchApiForShow() {
      RestTemplate restTemplate = new RestTemplate();
      ContentService contentService = new ContentService(restTemplate);
      String searchTerm = "gamE of thrones";

      List<ShowSearchData> searchResult = contentService.searchForShow(searchTerm);

      assertThat(searchResult.get(0).getApiId()).isEqualTo(1399);
      assertThat(searchResult.get(0).getName()).isEqualToIgnoringCase("game of Thrones");
   }
}