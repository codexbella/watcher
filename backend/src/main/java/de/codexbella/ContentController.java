package de.codexbella;

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
   public List<ShowSearchData> searchForShow(@PathVariable String searchTerm) {
      List<ShowSearchData> shows = contentService.searchForShow(searchTerm);
      return shows;
   }
}
