package de.codexbella;

import de.codexbella.content.ContentMapper;
import de.codexbella.content.ShowApi;
import de.codexbella.search.ShowSearchData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ContentServiceTest {

   @Test
   void shouldSearchMockApiForShowWithOnePageResult() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper);
      String searchTerm = "game+of+thrones";
      when(mockApi.getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm, String.class))
            .thenReturn(searchResultOnePage);

      List<ShowSearchData> searchResult = contentService.searchForShows("en-US",searchTerm);

      assertThat(searchResult.size()).isEqualTo(2);
      assertThat(searchResult.get(0).getApiId()).isEqualTo(1399);
      assertThat(searchResult.get(0).getName()).isEqualTo("Game of Thrones");
      assertThat(searchResult.get(1).getApiId()).isEqualTo(138757);
      assertThat(searchResult.get(1).getName()).isEqualTo("Autópsia Game Of Thrones");
   }
   @Test
   void shouldSearchMockApiForShowWithTwoPageResult() {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper);
      String searchTerm = "voyager";
      when(mockApi.getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm, String.class))
            .thenReturn(searchResultTwoPageFirstPage);
      when(mockApi.getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm+"&page=2", String.class))
            .thenReturn(searchResultTwoPageSecondPage);

      List<ShowSearchData> searchResult = contentService.searchForShows("en-US",searchTerm);

      assertThat(searchResult.size()).isEqualTo(25);
      assertThat(searchResult.get(0).getApiId()).isEqualTo(1855);
      assertThat(searchResult.get(0).getName()).isEqualTo("Star Trek: Voyager");
      assertThat(searchResult.get(1).getApiId()).isEqualTo(76931);
      assertThat(searchResult.get(1).getName()).isEqualTo("Voyager");

      assertThat(searchResult.get(20).getApiId()).isEqualTo(132372);
      assertThat(searchResult.get(20).getName()).isEqualTo("Jules Verne's Amazing Journeys");
      assertThat(searchResult.get(21).getApiId()).isEqualTo(98570);
      assertThat(searchResult.get(21).getName()).isEqualTo("Voyages au Pays des Vins de Terroir");

      verify(mockApi).getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm, String.class);
      verify(mockApi).getForObject("https://api.themoviedb.org/3/search/tv?api_key=xxx&language=en-US&query="+searchTerm+"&page=2", String.class);
      verifyNoMoreInteractions(mockApi);
   }
   @Test
   void shouldSaveShow() throws IllegalArgumentException {
      ContentMapper contentMapper = new ContentMapper();
      ShowRepository mockShowRepo = Mockito.mock(ShowRepository.class);
      RestTemplate mockApi = Mockito.mock(RestTemplate.class);
      ContentService contentService = new ContentService("xxx", mockApi, mockShowRepo, contentMapper);
      when(mockApi.getForObject("https://api.themoviedb.org/3/tv/1855?api_key=xxx&language=en-US", String.class))
            .thenReturn(searchResultVoyager);

      contentService.saveShow("en-US", 1855, "testuser");

      verify(mockApi).getForObject("https://api.themoviedb.org/3/tv/1855?api_key=xxx&language=en-US", String.class);
      verifyNoMoreInteractions(mockApi);

      verify(mockShowRepo).findByApiId(1855);
      verify(mockShowRepo).save(any());
      verifyNoMoreInteractions(mockShowRepo);
   }

   private final String searchResultOnePage = "{\n" +
         "    \"page\": 1,\n" +
         "    \"results\": [\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/suopoADq0k8YZr4dQXcU6pToj6s.jpg\",\n" +
         "            \"first_air_date\": \"2011-04-17\",\n" +
         "            \"genre_ids\": [\n" +
         "                10765,\n" +
         "                18,\n" +
         "                10759\n" +
         "            ],\n" +
         "            \"id\": 1399,\n" +
         "            \"name\": \"Game of Thrones\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Game of Thrones\",\n" +
         "            \"overview\": \"Seven noble families fight for control of the mythical land of Westeros. " +
         "Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest " +
         "north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between" +
         " the realms of men and icy horrors beyond.\",\n" +
         "            \"popularity\": 576.826,\n" +
         "            \"poster_path\": \"/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg\",\n" +
         "            \"vote_average\": 8.4,\n" +
         "            \"vote_count\": 17601\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2020-04-14\",\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 138757,\n" +
         "            \"name\": \"Autópsia Game Of Thrones\",\n" +
         "            \"origin_country\": [],\n" +
         "            \"original_language\": \"pt\",\n" +
         "            \"original_name\": \"Autópsia Game Of Thrones\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 2.415,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        }\n" +
         "    ],\n" +
         "    \"total_pages\": 1,\n" +
         "    \"total_results\": 2\n" +
         "}";
   private final String searchResultTwoPageFirstPage = "{\n" +
         "    \"page\": 1,\n" +
         "    \"results\": [\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/7YFranrnnIcCrgsLYQsoq8aE3Ir.jpg\",\n" +
         "            \"first_air_date\": \"1995-01-16\",\n" +
         "            \"genre_ids\": [\n" +
         "                10765,\n" +
         "                18,\n" +
         "                10759\n" +
         "            ],\n" +
         "            \"id\": 1855,\n" +
         "            \"name\": \"Star Trek: Voyager\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Star Trek: Voyager\",\n" +
         "            \"overview\": \"Pulled to the far side of the galaxy, where the Federation is 75 years away at " +
         "maximum warp speed, a Starfleet ship must cooperate with Maquis rebels to find a way home.\",\n" +
         "            \"popularity\": 89.321,\n" +
         "            \"poster_path\": \"/5iROn4oot6R0kkpWD6oJdHB15ZU.jpg\",\n" +
         "            \"vote_average\": 7.9,\n" +
         "            \"vote_count\": 615\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2003-05-20\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 76931,\n" +
         "            \"name\": \"Voyager\",\n" +
         "            \"origin_country\": [\n" +
         "                \"IT\"\n" +
         "            ],\n" +
         "            \"original_language\": \"it\",\n" +
         "            \"original_name\": \"Voyager\",\n" +
         "            \"overview\": \"An Italian show about the unexplained\",\n" +
         "            \"popularity\": 1.4,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 10,\n" +
         "            \"vote_count\": 1\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"1988-01-17\",\n" +
         "            \"genre_ids\": [\n" +
         "                10759\n" +
         "            ],\n" +
         "            \"id\": 6082,\n" +
         "            \"name\": \"Earth Star Voyager\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Earth Star Voyager\",\n" +
         "            \"overview\": \"Earth Star Voyager is the name of a science fiction television movie shown on " +
         "the Wonderful World of Disney in 1988. The show aired as a two-part pilot, but was never picked up for a " +
         "series and has not been released on DVD, although a fan base for the pilot has grown over the years.\",\n" +
         "            \"popularity\": 1.848,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2016-12-25\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 66261,\n" +
         "            \"name\": \"VOYAGER\",\n" +
         "            \"origin_country\": [\n" +
         "                \"IN\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"VOYAGER\",\n" +
         "            \"overview\": \"A series that documents the real life experience of humans and the effects on " +
         "each us.\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/9HR52WUP1kVHqYIqEYTfbacGi41.jpg\",\n" +
         "            \"first_air_date\": \"2016-10-08\",\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 138672,\n" +
         "            \"name\": \"The Voyager with Josh Garcia\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"ko\",\n" +
         "            \"original_name\": \"The Voyager with Josh Garcia\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": \"/dxqq7BD463wfolfYz60C35floN3.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/dUgyFW400ddeu5CjaK2l1M8J6LM.jpg\",\n" +
         "            \"first_air_date\": \"1982-10-03\",\n" +
         "            \"genre_ids\": [\n" +
         "                10765\n" +
         "            ],\n" +
         "            \"id\": 5391,\n" +
         "            \"name\": \"Voyagers\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Voyagers\",\n" +
         "            \"overview\": \"Voyagers! is an American science fiction time travel-based television series " +
         "that aired on NBC during the 1982–1983 season. The series stars Jon-Erik Hexum and Meeno Peluce.\",\n" +
         "            \"popularity\": 10.702,\n" +
         "            \"poster_path\": \"/dVoyDxmQnpbCExQa1KAGQ4KUdbF.jpg\",\n" +
         "            \"vote_average\": 7.5,\n" +
         "            \"vote_count\": 67\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2004-10-08\",\n" +
         "            \"genre_ids\": [\n" +
         "                18,\n" +
         "                10759,\n" +
         "                10765\n" +
         "            ],\n" +
         "            \"id\": 4653,\n" +
         "            \"name\": \"Star Trek: Phase II\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Star Trek: Phase II\",\n" +
         "            \"overview\": \"Star Trek: Phase II is a fan-created science fiction series set in the Star " +
         "Trek universe. The series was created by James Cawley and Jack Marshall in April 2003. The series, released" +
         " exclusively via the Internet, is designed as a continuation of the original Star Trek, beginning in the " +
         "fifth and final year of the starship Enterprise's \\\"five-year mission.\\\" The first episode of the " +
         "series was released in January 2004, with new episodes being released at a rate of about one per year, " +
         "though producers have expressed their desire to accelerate production.\\n\\nCBS, which owns the legal " +
         "rights to the Star Trek franchise, allows the distribution of fan-created material as long as no attempt is" +
         " made to profit from it without official authorization, and Phase II enjoys the same tolerance.\\n\\nStar " +
         "Trek: Phase II stars James Cawley as Captain Kirk, Brandon Stacy as Mr. Spock, and John Kelley as Dr. McCoy" +
         ". Eugene Roddenberry Jr., the son of Star Trek creator Gene Roddenberry, serves as consulting producer. " +
         "Some of the original actors have returned to reprise their roles, including George Takei as Sulu in " +
         "\\\"World Enough and Time\\\", and Walter Koenig as Chekov in \\\"To Serve All My Days\\\". The episodes " +
         "are filmed on new sets located in Port Henry, NY, at a long-shuttered car dealership.\",\n" +
         "            \"popularity\": 6.386,\n" +
         "            \"poster_path\": \"/5S46d3jrIikTMPifzO7a8zBBCLC.jpg\",\n" +
         "            \"vote_average\": 6.9,\n" +
         "            \"vote_count\": 7\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/nz8TI2O4ZJPYPOxwunApHhiKehU.jpg\",\n" +
         "            \"first_air_date\": \"1999-06-25\",\n" +
         "            \"genre_ids\": [\n" +
         "                35,\n" +
         "                10765\n" +
         "            ],\n" +
         "            \"id\": 105645,\n" +
         "            \"name\": \"Tenamonya Voyagers\",\n" +
         "            \"origin_country\": [],\n" +
         "            \"original_language\": \"ja\",\n" +
         "            \"original_name\": \"てなもんやボイジャーズ\",\n" +
         "            \"overview\": \"Their school shut down, Ayako and Wakana are now stranded far from Earth. Enter" +
         " Paraila, a wanted criminal, and they believe they can make it back.\",\n" +
         "            \"popularity\": 0.871,\n" +
         "            \"poster_path\": \"/tI4D0wXDJYBwbYIaD5oftH4OUhO.jpg\",\n" +
         "            \"vote_average\": 9,\n" +
         "            \"vote_count\": 1\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/3zD7vNORpL5HjrOca55mIrZTkV4.jpg\",\n" +
         "            \"first_air_date\": \"1993-09-06\",\n" +
         "            \"genre_ids\": [\n" +
         "                16,\n" +
         "                10759,\n" +
         "                10762\n" +
         "            ],\n" +
         "            \"id\": 128416,\n" +
         "            \"name\": \"Corentin\",\n" +
         "            \"origin_country\": [\n" +
         "                \"FR\"\n" +
         "            ],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Corentin\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 2.638,\n" +
         "            \"poster_path\": \"/ddedzgzdzWPRG1SAMb7fDvCfJtz.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/qkX22ZtQ9QxoBKRKMdvjkinCSR7.jpg\",\n" +
         "            \"first_air_date\": \"2018-04-29\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 96001,\n" +
         "            \"name\": \"Die Reise der Menschheit\",\n" +
         "            \"origin_country\": [\n" +
         "                \"DE\"\n" +
         "            ],\n" +
         "            \"original_language\": \"de\",\n" +
         "            \"original_name\": \"Die Reise der Menschheit\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": \"/tufiuKDm9F16Eu7ZSOvQ0N3Yv4I.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/tFAU3OAJDg5WGm1iUQ3wtdzl11v.jpg\",\n" +
         "            \"first_air_date\": \"1996-01-01\",\n" +
         "            \"genre_ids\": [\n" +
         "                16\n" +
         "            ],\n" +
         "            \"id\": 43908,\n" +
         "            \"name\": \"The Fantastic Voyages of Sinbad the Sailor\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"The Fantastic Voyages of Sinbad the Sailor\",\n" +
         "            \"overview\": \"The Fantastic Voyages of Sinbad the Sailor is a television series that aired " +
         "during 1996–1998 on Cartoon Network. It was animated by Fred Wolf Films.\\n\\nThe series had Sinbad as a " +
         "teenager with a exotic cat cub and a young boy as constant companions.\",\n" +
         "            \"popularity\": 4.572,\n" +
         "            \"poster_path\": \"/13E3BbXjLAExmIflIXFBbxwA0TG.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/3juvAjSEU4JqViku7mAWBdr3CHq.jpg\",\n" +
         "            \"first_air_date\": \"2021-09-23\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 152532,\n" +
         "            \"name\": \"Journey into Maths Country\",\n" +
         "            \"origin_country\": [\n" +
         "                \"FR\"\n" +
         "            ],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Voyages au pays des maths\",\n" +
         "            \"overview\": \"Math is an exotic and confusing country. We speak a bizarre language, full of " +
         "homeomorphisms, differential varieties, transfinite numbers. But we also find epic landscapes, dizzying " +
         "ideas and even, sometimes, useful things!\",\n" +
         "            \"popularity\": 1.285,\n" +
         "            \"poster_path\": \"/8HwCcPrhiTGy4YbGXlNTB5NKzvw.jpg\",\n" +
         "            \"vote_average\": 10,\n" +
         "            \"vote_count\": 1\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/kA2NAZ4bW6vESaIjCiWJxPsKEdM.jpg\",\n" +
         "            \"first_air_date\": \"1996-02-04\",\n" +
         "            \"genre_ids\": [\n" +
         "                10759,\n" +
         "                10765,\n" +
         "                18\n" +
         "            ],\n" +
         "            \"id\": 13675,\n" +
         "            \"name\": \"Gulliver's Travels\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Gulliver's Travels\",\n" +
         "            \"overview\": \"Dr. Gulliver has returned from his journey to his family after a long absence -" +
         " and tells them the story of his travels.\",\n" +
         "            \"popularity\": 14.515,\n" +
         "            \"poster_path\": \"/fBwZWaxVlAXovH3dhv3BtIjtu2O.jpg\",\n" +
         "            \"vote_average\": 6.9,\n" +
         "            \"vote_count\": 80\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2013-12-23\",\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 65308,\n" +
         "            \"name\": \"Space Voyages\",\n" +
         "            \"origin_country\": [],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Space Voyages\",\n" +
         "            \"overview\": \"Journey behind the scenes, for a rare glimpse inside NASA's World of high " +
         "technology and advancement of space travel. Unravel the history of mankind's incredible flying machine,the " +
         "space shuttle, and meet the dedicated team of experts responsible for the missions beyond our Earth\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/6Ltkzy3UGoplX8QVRYtaAYyXQ8n.jpg\",\n" +
         "            \"first_air_date\": \"2020-09-01\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 114684,\n" +
         "            \"name\": \"Wonders of Northern Europe\",\n" +
         "            \"origin_country\": [\n" +
         "                \"FR\"\n" +
         "            ],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Voyages en terres du nord\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 1.063,\n" +
         "            \"poster_path\": \"/8w38nw4cj8JzrgVCf2xKYJ5r1qt.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2006-11-23\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 23029,\n" +
         "            \"name\": \"Voyages Of Discovery\",\n" +
         "            \"origin_country\": [\n" +
         "                \"GB\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Voyages Of Discovery\",\n" +
         "            \"overview\": \"Series in which explorer Paul Rose celebrates the achievements of famous seamen" +
         " and explorers\",\n" +
         "            \"popularity\": 0.828,\n" +
         "            \"poster_path\": \"/sc2sEOJY4jM6fWxzPyuycGAqDka.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/5p3YvlfqLzYYfrKkD3GESJKdjLJ.jpg\",\n" +
         "            \"first_air_date\": \"2017-09-16\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 84030,\n" +
         "            \"name\": \"Journeys Through French Cinema\",\n" +
         "            \"origin_country\": [\n" +
         "                \"FR\"\n" +
         "            ],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Voyage à travers le cinéma français\",\n" +
         "            \"overview\": \"My Journey Through French Cinema (2017), Bertrand Tavernier’s César-nominated " +
         "three-and-a-half-hour tour through French film history, was too short to introduce audiences to all that he" +
         " wanted to share. In this new eight-part series (8x55min), the acclaimed director of such films as Coup de " +
         "Torchon and ‘Round Midnight guides us through a roster of filmmakers both influential and forgotten, " +
         "explores how his country’s cinema was shaped by the German occupation and changed again through the New " +
         "Wave, spotlights little-known female filmmakers, and more. Subjects include: René Clément, Henri-Georges " +
         "Clouzot, Julien Duvivier, Henri Decoin, Claude Autant-Lara, as well as composers who made movie music an " +
         "art in and of itself, far from the Hollywood spotlight.\",\n" +
         "            \"popularity\": 1.016,\n" +
         "            \"poster_path\": \"/sUk4Zr1jLBBaMnkbenUPKbHcTTs.jpg\",\n" +
         "            \"vote_average\": 8,\n" +
         "            \"vote_count\": 3\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"1992-09-08\",\n" +
         "            \"genre_ids\": [\n" +
         "                16\n" +
         "            ],\n" +
         "            \"id\": 22785,\n" +
         "            \"name\": \"Gulliver's Travels\",\n" +
         "            \"origin_country\": [\n" +
         "                \"FR\",\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Gulliver's Travels\",\n" +
         "            \"overview\": \"Follows the fortunes of sailor Dr. Lemuel Gulliver who decided to explore the " +
         "whole world. During his travel Gulliver meets the world, and he's accompanied by a Dr. Flim, his wife " +
         "Fosla, her daughter Folia and best friend Raphael.\",\n" +
         "            \"popularity\": 1.4,\n" +
         "            \"poster_path\": \"/bvSiVnlVe48nxlwz81x9LDGNt7e.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 13416,\n" +
         "            \"name\": \"Les Voyages du tortillard\",\n" +
         "            \"origin_country\": [\n" +
         "                \"CA\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Les Voyages du tortillard\",\n" +
         "            \"overview\": \"Les Voyages du tortillard was a French language series of animated shorts made " +
         "in Quebec.\\n\\nThe English version aired in Canada on the Global Television Network and TVOntario in the " +
         "late 1970s, and in the United States on The Great Space Coaster in the early 1980s.\\n\\nThe main character" +
         " is a young boy named Simon who discovers a magical steam locomotive hidden in the basement of his " +
         "apartment building. Acting as the engineer Simon uses the locomotive to travel on many magical adventures" +
         ".\\n\\nJoining Simon every episode is Monsieur Globetrotteur, an old man who awaited the coming of the " +
         "train for many a decade before Simon finally arrived. With Mr. Passenger is Mélanie, Mr. Passenger's cat " +
         "who always rides atop his long stove pipe hat.\\n\\nEach episode also features a character named Stella, a " +
         "girl with star shaped hair. Stella's character changes from episode to episode but she always has her star " +
         "shaped hair.\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2000-01-01\",\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 97063,\n" +
         "            \"name\": \"Les Voyages de Balthazar\",\n" +
         "            \"origin_country\": [],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Les Voyages de Balthazar\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        }\n" +
         "    ],\n" +
         "    \"total_pages\": 2,\n" +
         "    \"total_results\": 25\n" +
         "}";
   private final String searchResultTwoPageSecondPage = "{\n" +
         "    \"page\": 2,\n" +
         "    \"results\": [\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"2000-12-24\",\n" +
         "            \"genre_ids\": [\n" +
         "                16,\n" +
         "                10765,\n" +
         "                10759\n" +
         "            ],\n" +
         "            \"id\": 132372,\n" +
         "            \"name\": \"Jules Verne's Amazing Journeys\",\n" +
         "            \"origin_country\": [\n" +
         "                \"FR\"\n" +
         "            ],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Les Voyages extraordinaires de Jules Verne\",\n" +
         "            \"overview\": \"Jules Verne’s exciting, imaginative classics are among the unbeatable book " +
         "experiences of several generations. We love the characters, we get excited about them as we get to know the" +
         " different peoples of the Earth, and with the help of our imagination it flies us all over the world.\",\n" +
         "            \"popularity\": 1.161,\n" +
         "            \"poster_path\": \"/907J7US3mNH4A6MQZzCT0C0OSlo.jpg\",\n" +
         "            \"vote_average\": 7,\n" +
         "            \"vote_count\": 1\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"\",\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 98570,\n" +
         "            \"name\": \"Voyages au Pays des Vins de Terroir\",\n" +
         "            \"origin_country\": [],\n" +
         "            \"original_language\": \"fr\",\n" +
         "            \"original_name\": \"Voyages au Pays des Vins de Terroir\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": null,\n" +
         "            \"first_air_date\": \"\",\n" +
         "            \"genre_ids\": [],\n" +
         "            \"id\": 39976,\n" +
         "            \"name\": \"The Voyages of Young Doctor Dolittle\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"The Voyages of Young Doctor Dolittle\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": null,\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/rz4uGR4FBwllOISGL0rw0fJl1YH.jpg\",\n" +
         "            \"first_air_date\": \"1988-10-21\",\n" +
         "            \"genre_ids\": [\n" +
         "                16\n" +
         "            ],\n" +
         "            \"id\": 12152,\n" +
         "            \"name\": \"This Is America, Charlie Brown\",\n" +
         "            \"origin_country\": [\n" +
         "                \"US\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"This Is America, Charlie Brown\",\n" +
         "            \"overview\": \"The Peanuts gang visit important events in United States history.\",\n" +
         "            \"popularity\": 3.441,\n" +
         "            \"poster_path\": \"/1q745vkaoHZ9F9zaElmbUK6cwsV.jpg\",\n" +
         "            \"vote_average\": 7,\n" +
         "            \"vote_count\": 14\n" +
         "        },\n" +
         "        {\n" +
         "            \"backdrop_path\": \"/n2NQnm7gqJFVCOcoKeaMbKgAEjH.jpg\",\n" +
         "            \"first_air_date\": \"1971-12-29\",\n" +
         "            \"genre_ids\": [\n" +
         "                99\n" +
         "            ],\n" +
         "            \"id\": 123375,\n" +
         "            \"name\": \"Attenborough in Paradise and Other Personal Voyages\",\n" +
         "            \"origin_country\": [\n" +
         "                \"GB\"\n" +
         "            ],\n" +
         "            \"original_language\": \"en\",\n" +
         "            \"original_name\": \"Attenborough in Paradise and Other Personal Voyages\",\n" +
         "            \"overview\": \"A collection of seven David Attenborough specials, Attenborough in Paradise " +
         "represents some of the famed naturalist's most personal quests and passionate enthusiasms. Programming " +
         "includes Attenborough tracing a piece of amber in The Amber Time Machine, discovering the history behind a " +
         "strange figurine in The Lost Gods of Easter Island, and realizing a childhood dream by visiting New Guinea " +
         "to record the spectacular courtship displays of the birds of paradise. The set also includes Life on Air, a" +
         " 2002 tribute to Attenborough, chronicling the world-renowned natural history expert's 50-year career at " +
         "the BBC, presented by Michael Palin\",\n" +
         "            \"popularity\": 0.6,\n" +
         "            \"poster_path\": \"/yustSwJWh5WIEXAX7jVxbC0q7nQ.jpg\",\n" +
         "            \"vote_average\": 0,\n" +
         "            \"vote_count\": 0\n" +
         "        }\n" +
         "    ],\n" +
         "    \"total_pages\": 2,\n" +
         "    \"total_results\": 25\n" +
         "}";
   private final String searchResultVoyager = "{\n" +
         "    \"adult\": false,\n" +
         "    \"backdrop_path\": \"/7YFranrnnIcCrgsLYQsoq8aE3Ir.jpg\",\n" +
         "    \"created_by\": [\n" +
         "        {\n" +
         "            \"id\": 1745,\n" +
         "            \"credit_id\": \"5257177419c295711409ca4e\",\n" +
         "            \"name\": \"Gene Roddenberry\",\n" +
         "            \"gender\": 2,\n" +
         "            \"profile_path\": \"/qY3gWqAMDrrvNVUrwZ8lSa4IKtS.jpg\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"id\": 2514,\n" +
         "            \"credit_id\": \"5dbd6a069638640014e14d24\",\n" +
         "            \"name\": \"Michael Piller\",\n" +
         "            \"gender\": 2,\n" +
         "            \"profile_path\": \"/gRVdvhnkO93FBIq4GuIWcw5scmD.jpg\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"id\": 1213783,\n" +
         "            \"credit_id\": \"5257177419c295711409ca48\",\n" +
         "            \"name\": \"Rick Berman\",\n" +
         "            \"gender\": 2,\n" +
         "            \"profile_path\": \"/mEznRUFYLmpD6jPPeZKLWkHtPdn.jpg\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"id\": 1219348,\n" +
         "            \"credit_id\": \"5257177419c295711409ca42\",\n" +
         "            \"name\": \"Jeri Taylor\",\n" +
         "            \"gender\": 1,\n" +
         "            \"profile_path\": null\n" +
         "        }\n" +
         "    ],\n" +
         "    \"episode_run_time\": [\n" +
         "        45,\n" +
         "        60\n" +
         "    ],\n" +
         "    \"first_air_date\": \"1995-01-16\",\n" +
         "    \"genres\": [\n" +
         "        {\n" +
         "            \"id\": 10765,\n" +
         "            \"name\": \"Sci-Fi & Fantasy\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"id\": 18,\n" +
         "            \"name\": \"Drama\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"id\": 10759,\n" +
         "            \"name\": \"Action & Adventure\"\n" +
         "        }\n" +
         "    ],\n" +
         "    \"homepage\": \"https://www.startrek.com/shows/star-trek-voyager\",\n" +
         "    \"id\": 1855,\n" +
         "    \"in_production\": false,\n" +
         "    \"languages\": [\n" +
         "        \"en\"\n" +
         "    ],\n" +
         "    \"last_air_date\": \"2001-05-23\",\n" +
         "    \"last_episode_to_air\": {\n" +
         "        \"air_date\": \"2001-05-23\",\n" +
         "        \"episode_number\": 26,\n" +
         "        \"id\": 1132734,\n" +
         "        \"name\": \"Endgame (2)\",\n" +
         "        \"overview\": \"Stardate: Unknown. After a decades-long journey to reach the Alpha Quadrant, " +
         "Admiral Kathryn Janeway makes a bold decision to change the past in an attempt to undo the toll taken on " +
         "the crew during their arduous journey home.\\n\\nThis is the final episode of the series.\",\n" +
         "        \"production_code\": \"\",\n" +
         "        \"season_number\": 7,\n" +
         "        \"still_path\": \"/6dtn51OtnndcEx43ey3YhFiYdZT.jpg\",\n" +
         "        \"vote_average\": 7.8,\n" +
         "        \"vote_count\": 5\n" +
         "    },\n" +
         "    \"name\": \"Star Trek: Voyager\",\n" +
         "    \"next_episode_to_air\": null,\n" +
         "    \"networks\": [\n" +
         "        {\n" +
         "            \"name\": \"UPN\",\n" +
         "            \"id\": 40,\n" +
         "            \"logo_path\": \"/333LtWX9Z7H9uRrNcCl1JcTvdpR.png\",\n" +
         "            \"origin_country\": \"US\"\n" +
         "        }\n" +
         "    ],\n" +
         "    \"number_of_episodes\": 172,\n" +
         "    \"number_of_seasons\": 7,\n" +
         "    \"origin_country\": [\n" +
         "        \"US\"\n" +
         "    ],\n" +
         "    \"original_language\": \"en\",\n" +
         "    \"original_name\": \"Star Trek: Voyager\",\n" +
         "    \"overview\": \"Pulled to the far side of the galaxy, where the Federation is 75 years away at maximum " +
         "warp speed, a Starfleet ship must cooperate with Maquis rebels to find a way home.\",\n" +
         "    \"popularity\": 89.321,\n" +
         "    \"poster_path\": \"/5iROn4oot6R0kkpWD6oJdHB15ZU.jpg\",\n" +
         "    \"production_companies\": [\n" +
         "        {\n" +
         "            \"id\": 9223,\n" +
         "            \"logo_path\": \"/of4mmVt6egYaO9oERJbuUxMOTkj.png\",\n" +
         "            \"name\": \"Paramount Television Studios\",\n" +
         "            \"origin_country\": \"US\"\n" +
         "        }\n" +
         "    ],\n" +
         "    \"production_countries\": [\n" +
         "        {\n" +
         "            \"iso_3166_1\": \"US\",\n" +
         "            \"name\": \"United States of America\"\n" +
         "        }\n" +
         "    ],\n" +
         "    \"seasons\": [\n" +
         "        {\n" +
         "            \"air_date\": \"1995-01-09\",\n" +
         "            \"episode_count\": 90,\n" +
         "            \"id\": 5314,\n" +
         "            \"name\": \"Specials\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/86HvbmgLzdRZQw9MrAkENwjBgXz.jpg\",\n" +
         "            \"season_number\": 0\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"1995-01-16\",\n" +
         "            \"episode_count\": 16,\n" +
         "            \"id\": 5307,\n" +
         "            \"name\": \"Season 1\",\n" +
         "            \"overview\": \"Pulled to the far side of the galaxy, where the Federation is 75 years away at " +
         "maximum warp speed, a Starfleet ship must cooperate with Maquis rebels to find a way home.\",\n" +
         "            \"poster_path\": \"/7XtNLDCcKQe6N11X1cyrJRYl4JE.jpg\",\n" +
         "            \"season_number\": 1\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"1995-08-28\",\n" +
         "            \"episode_count\": 26,\n" +
         "            \"id\": 5308,\n" +
         "            \"name\": \"Season 2\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/lun9USO2YwAByRFxEzo2eygtfhx.jpg\",\n" +
         "            \"season_number\": 2\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"1996-09-04\",\n" +
         "            \"episode_count\": 26,\n" +
         "            \"id\": 5309,\n" +
         "            \"name\": \"Season 3\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/aEWmLsXPQKKTzFkHZhuxrxYhfkH.jpg\",\n" +
         "            \"season_number\": 3\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"1997-09-03\",\n" +
         "            \"episode_count\": 26,\n" +
         "            \"id\": 5310,\n" +
         "            \"name\": \"Season 4\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/4ALy9tEwhGoELY13qXKTkeTxVtH.jpg\",\n" +
         "            \"season_number\": 4\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"1998-10-14\",\n" +
         "            \"episode_count\": 26,\n" +
         "            \"id\": 5311,\n" +
         "            \"name\": \"Season 5\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/lOcILt6umTmgbeDu9Xc3eV0F7u0.jpg\",\n" +
         "            \"season_number\": 5\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"1999-09-22\",\n" +
         "            \"episode_count\": 26,\n" +
         "            \"id\": 5312,\n" +
         "            \"name\": \"Season 6\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/xUS4prP6vuNzhRkkdEdW1LSzsQj.jpg\",\n" +
         "            \"season_number\": 6\n" +
         "        },\n" +
         "        {\n" +
         "            \"air_date\": \"2000-10-04\",\n" +
         "            \"episode_count\": 26,\n" +
         "            \"id\": 5313,\n" +
         "            \"name\": \"Season 7\",\n" +
         "            \"overview\": \"\",\n" +
         "            \"poster_path\": \"/4dWUr7QahimtvwNeQPd73bM6f4t.jpg\",\n" +
         "            \"season_number\": 7\n" +
         "        }\n" +
         "    ],\n" +
         "    \"spoken_languages\": [\n" +
         "        {\n" +
         "            \"english_name\": \"English\",\n" +
         "            \"iso_639_1\": \"en\",\n" +
         "            \"name\": \"English\"\n" +
         "        }\n" +
         "    ],\n" +
         "    \"status\": \"Ended\",\n" +
         "    \"tagline\": \"Charting the new frontier\",\n" +
         "    \"type\": \"Scripted\",\n" +
         "    \"vote_average\": 7.9,\n" +
         "    \"vote_count\": 615\n" +
         "}";
}
