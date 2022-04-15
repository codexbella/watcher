package de.codexbella.content;

import de.codexbella.content.episode.EpisodeInSeason;
import de.codexbella.content.episode.EpisodeInSeasonApi;
import de.codexbella.content.episode.EpisodeInShow;
import de.codexbella.content.episode.EpisodeInShowApi;
import de.codexbella.content.season.Season;
import de.codexbella.content.season.SeasonApi;
import de.codexbella.content.season.SeasonInShow;
import de.codexbella.content.season.SeasonInShowApi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentMapper {
   public Show toShow(ShowApi showApi) {
      Show show = new Show();
      show.setApiId(showApi.getApiId());
      show.setName(showApi.getName());
      show.setOverview(showApi.getOverview());
      show.setAirDate(showApi.getAirDate());
      show.setOriginCountry(showApi.getOriginCountry());
      show.setLanguages(showApi.getLanguages());
      show.setTagline(showApi.getTagline());
      show.setGenres(showApi.getGenres());
      show.setVoteAverage(showApi.getVoteAverage());
      show.setVoteCount(showApi.getVoteCount());
      show.setPosterPath(showApi.getPosterPath());
      show.setSeasonInShows(toSeasonInShowList(showApi.getSeasons()));
      return show;
   }

   private List<SeasonInShow> toSeasonInShowList(List<SeasonInShowApi> seasonApiList) {
      return seasonApiList.stream().map(seasonApi -> toSeasonInShow(seasonApi)).toList();
   }

   public SeasonInShow toSeasonInShow(SeasonInShowApi seasonApi) {
      SeasonInShow seasonInShow = new SeasonInShow();
      seasonInShow.setApiId(seasonApi.getApiId());
      seasonInShow.setSeasonNumber(seasonApi.getSeasonNumber());
      seasonInShow.setName(seasonApi.getName());
      seasonInShow.setNumberOfEpisodes(seasonApi.getNumberOfEpisodes());
      seasonInShow.setOverview(seasonApi.getOverview());
      seasonInShow.setPosterPath(seasonApi.getPosterPath());
      seasonInShow.setEpisodes(toEpisodeInShowList(seasonApi.getEpisodes()));
      return seasonInShow;
   }

   private List<EpisodeInShow> toEpisodeInShowList(List<EpisodeInShowApi> episodeApiList) {
      return episodeApiList.stream().map(episodeApi -> toEpisodeInShow(episodeApi)).toList();
   }
   
   private EpisodeInShow toEpisodeInShow(EpisodeInShowApi episodeApi) {
      EpisodeInShow episodeInShow = new EpisodeInShow();
      episodeInShow.setApiId(episodeApi.getApiId());
      episodeInShow.setSeasonNumber(episodeApi.getSeasonNumber());
      episodeInShow.setName(episodeApi.getName());
      episodeInShow.setOverview(episodeApi.getOverview());
      episodeInShow.setVoteAverage(episodeApi.getVoteAverage());
      episodeInShow.setVoteCount(episodeApi.getVoteCount());
      episodeInShow.setStillPath(episodeApi.getStillPath());
      return episodeInShow;
   }

   public Season toSeason(SeasonApi seasonApi) {
      Season season = new Season();
      season.setApiId(seasonApi.getApiId());
      season.setName(seasonApi.getName());
      season.setOverview(seasonApi.getOverview());
      season.setAirDate(seasonApi.getAirDate());
      season.setSeasonNumber(seasonApi.getSeasonNumber());
      season.setPosterPath(seasonApi.getPosterPath());
      season.setEpisodes(toEpisodeInSeasonList(seasonApi.getEpisodes()));
      return season;
   }
   private List<EpisodeInSeason> toEpisodeInSeasonList(List<EpisodeInSeasonApi> episodeInSeasonApiList) {
      return episodeInSeasonApiList.stream().map(episodeInSeasonApi -> toEpisodeInSeason(episodeInSeasonApi)).toList();
   }
   private EpisodeInSeason toEpisodeInSeason(EpisodeInSeasonApi episodeInSeasonApi) {
      EpisodeInSeason episodeInSeason = new EpisodeInSeason();
      episodeInSeason.setApiId(episodeInSeasonApi.getApiId());
      episodeInSeason.setName(episodeInSeasonApi.getName());
      episodeInSeason.setOverview(episodeInSeasonApi.getOverview());
      episodeInSeason.setAirDate(episodeInSeasonApi.getAirDate());
      episodeInSeason.setEpisodeNumber(episodeInSeasonApi.getEpisodeNumber());
      episodeInSeason.setSeasonNumber(episodeInSeasonApi.getSeasonNumber());
      episodeInSeason.setVoteAverage(episodeInSeasonApi.getVoteAverage());
      episodeInSeason.setVoteCount(episodeInSeasonApi.getVoteCount());
      episodeInSeason.setStillPath(episodeInSeasonApi.getStillPath());
      return episodeInSeason;
   }
}
