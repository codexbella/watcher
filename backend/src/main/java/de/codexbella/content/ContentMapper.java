package de.codexbella.content;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
      List seasonList = new ArrayList();
      for (int i = 0; i < seasonApiList.size(); i++) {
         seasonList.add(toSeason(seasonApiList.get(i)));
      }
      return seasonList;
   }

   public Season toSeason(SeasonApi seasonApi) {
      Season season = new Season();
      season.setApiId(seasonApi.getApiId());
      season.setSeasonNumber(seasonApi.getSeasonNumber());
      season.setSeasonName(seasonApi.getSeasonName());
      season.setNumberOfEpisodes(seasonApi.getNumberOfEpisodes());
      season.setOverview(seasonApi.getOverview());
      season.setPosterPath(seasonApi.getPosterPath());
      season.setEpisodes(toEpisodeList(seasonApi.getEpisodes()));
      return season;
   }

   private List<Episode> toEpisodeList(List<EpisodeApi> episodeApiList) {
      List episodeList = new ArrayList();
      for (int i = 0; i < episodeApiList.size(); i++) {
         episodeList.add(toEpisode(episodeApiList.get(i)));
      }
      return episodeList;
   }
   
   private Episode toEpisode(EpisodeApi episodeApi) {
      Episode episode = new Episode();
      episode.setApiId(episodeApi.getApiId());
      episode.setSeasonNumber(episodeApi.getSeasonNumber());
      episode.setName(episodeApi.getName());
      episode.setOverview(episodeApi.getOverview());
      episode.setVoteAverage(episodeApi.getVoteAverage());
      episode.setVoteCount(episodeApi.getVoteCount());
      episode.setStillPath(episodeApi.getStillPath());
      return episode;
   }
}
