package de.codexbella;

import de.codexbella.content.Show;
import de.codexbella.search.ShowSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContentController {
   private final ContentService contentService;

   @GetMapping("/search/{searchTerm}")
   public List<ShowSearchData> searchForShows(@RequestParam(defaultValue = "en-US") String language,
                                              @PathVariable String searchTerm, Principal principal) {
      return contentService.searchForShows(language, searchTerm, principal.getName());
   }

   @PutMapping("/saveshow/{showApiId}")
   public ResponseEntity<String> saveShow(@RequestParam(defaultValue = "en-US") String language,
                                          @PathVariable int showApiId, Principal principal) {
      try {
         contentService.saveShow(language, showApiId, principal.getName());
         return new ResponseEntity<>("Show saved", HttpStatus.OK);
      } catch (IllegalArgumentException e) {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
   }

   @DeleteMapping("/deleteshow/{showApiId}")
   public void deleteShow(@PathVariable int showApiId, Principal principal) {
      contentService.deleteShow(showApiId, principal.getName());
   }

   @GetMapping("/getallshows")
   public List<Show> getAllShows(Principal principal) {
      return contentService.getAllShows(principal.getName());
   }
   @GetMapping("/getshow/{showId}")
   public ResponseEntity<Show> getShow(@PathVariable String showId, Principal principal) {
      return ResponseEntity.of(contentService.getShow(showId, principal.getName()));
   }
   @PutMapping("/saveseason/{showApiId}")
   public ResponseEntity<Show> saveSeason(@PathVariable int showApiId, @RequestParam(defaultValue = "en-US")
         String language, @RequestParam int seasonNumber, Principal principal) {
      Optional<Show> showOptional = contentService.saveSeason(language, showApiId, seasonNumber, principal.getName());
      return ResponseEntity.of(showOptional);
   }
}

