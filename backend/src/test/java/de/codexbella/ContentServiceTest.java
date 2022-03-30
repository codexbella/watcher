package de.codexbella;

import de.codexbella.search.SearchResultShows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class ContentServiceTest {
   @Autowired
   ContentService contentService;

   @Test
   void shouldSearchApiForShow() {
      RestTemplate restTemplate = new RestTemplate();
      ContentService contentService = new ContentService(restTemplate);
      String searchTerm = "gamE of thrones";

      SearchResultShows searchResult = contentService.searchForShow(searchTerm);

      assertThat(searchResult.getShows().get(0).getApiId()).isEqualTo(1399);
      assertThat(searchResult.getShows().get(0).getName()).isEqualToIgnoringCase("game of Thrones");
   }
}