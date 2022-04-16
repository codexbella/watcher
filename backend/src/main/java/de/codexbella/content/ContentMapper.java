package de.codexbella.content;

import de.codexbella.content.episode.Episode;
import de.codexbella.content.episode.EpisodeApi;
import de.codexbella.content.season.Season;
import de.codexbella.content.season.SeasonApi;
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
      show.setSeasons(toSeasonList(showApi.getSeasons()));
      return show;
   }

   private List<Season> toSeasonList(List<SeasonApi> seasonApiList) {
      return seasonApiList.stream().filter(seasonApi -> seasonApi.getSeasonNumber() != 0)
            .map(seasonApi -> toSeason(seasonApi)).toList();
   }
   public Season toSeason(SeasonApi seasonApi) {
      Season season = new Season();
      season.setApiId(seasonApi.getApiId());
      season.setName(seasonApi.getName());
      season.setOverview(seasonApi.getOverview());
      season.setAirDate(seasonApi.getAirDate());
      season.setSeasonNumber(seasonApi.getSeasonNumber());
      season.setPosterPath(seasonApi.getPosterPath());
      season.setNumberOfEpisodes(seasonApi.getNumberOfEpisodes());
      season.setEpisodes(toEpisodeList(seasonApi.getEpisodes()));
      return season;
   }
   private List<Episode> toEpisodeList(List<EpisodeApi> episodeApiList) {
      return episodeApiList.stream().map(episodeApi -> toEpisode(episodeApi)).toList();
   }
   private Episode toEpisode(EpisodeApi episodeApi) {
      Episode episode = new Episode();
      episode.setApiId(episodeApi.getApiId());
      episode.setName(episodeApi.getName());
      episode.setOverview(episodeApi.getOverview());
      episode.setAirDate(episodeApi.getAirDate());
      episode.setEpisodeNumber(episodeApi.getEpisodeNumber());
      episode.setSeasonNumber(episodeApi.getSeasonNumber());
      episode.setVoteAverage(episodeApi.getVoteAverage());
      episode.setVoteCount(episodeApi.getVoteCount());
      episode.setStillPath(episodeApi.getStillPath());
      return episode;
   }
}
