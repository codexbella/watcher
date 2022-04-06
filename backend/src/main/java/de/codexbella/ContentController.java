package de.codexbella;

import de.codexbella.content.Show;
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
   public List<ShowSearchData> searchForShow(@RequestParam(defaultValue = "en-US") String language, @PathVariable String searchTerm) {
      return contentService.searchForShows(language, searchTerm);
   }
   @GetMapping("/getshow/{id}")
   public Show searchForSingleShow(@RequestParam(defaultValue = "en-US") String language, @PathVariable int id) {
      return contentService.searchForSingleShow(language, id);
   }
}
