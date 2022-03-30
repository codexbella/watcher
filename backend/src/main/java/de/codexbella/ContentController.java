package de.codexbella;

import de.codexbella.search.ShowSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/watcher")
@RequiredArgsConstructor
public class ContentController {
   private final ContentService contentService;

   @GetMapping("/search={searchTerm}")
   public List<ShowSearchData> searchForShow(@PathVariable String searchTerm) {
      return contentService.searchForShow(searchTerm);
   }
}
