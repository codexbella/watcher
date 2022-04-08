package de.codexbella;

import de.codexbella.content.ShowApi;
import de.codexbella.search.ShowSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContentController {
   private final ContentService contentService;

   @GetMapping("/search/{searchTerm}")
   public List<ShowSearchData> searchForShows(@RequestParam(defaultValue = "en-US") String language, @PathVariable String searchTerm) {
      return contentService.searchForShows(language, searchTerm);
   }
   @GetMapping("/addshow/{apiId}")
   public ShowApi addShow(@RequestParam(defaultValue = "en-US") String language, @PathVariable int apiId) {
      return contentService.addShow(language, apiId);
   }
}
