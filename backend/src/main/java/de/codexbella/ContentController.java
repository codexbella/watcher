package de.codexbella;

import de.codexbella.search.ShowSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
   @GetMapping("/saveshow/{apiId}")
   public ResponseEntity<String> saveShow(@RequestParam(defaultValue = "en-US") String language, @PathVariable int apiId, Principal principal) {
      try {
         contentService.saveShow(language, apiId, principal.getName());
         return new ResponseEntity<>("Show saved", HttpStatus.OK);
      } catch (IllegalArgumentException e) {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
   }
}
