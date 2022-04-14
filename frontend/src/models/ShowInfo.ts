import {Seen} from "../Seen";

export interface ShowSearchData {
   apiId: number;
   name: string;
   overview: string;
   airDate: string;
   language: string;
   voteAverage: number;
   voteCount: number;
   posterPath: string;
   
   liked: boolean;
}

export interface ShowData {
   id: string;
   apiId: number;
   name: string;
   overview: string;
   airDate: string;
   originCountry: Array<string>;
   languages: Array<string>;
   tagline: string;
   genres: Array<Genre>;
   voteAverage: number;
   voteCount: number;
   posterPath: string;
   seasons: Array<Season>;
   username: string;
   seen: Seen;
   vote: number;
}

export interface Genre {
   apiId: number;
   name: string;
}
export interface Season {
   apiId: number;
   seasonNumber: number;
   seasonName: string;
   numberOfEpisodes: number;
   overview: string;
   posterPath: string;
   episodes: Array<Episode>;
   username: string;
   seen: Seen;
   vote: number;
}
export interface Episode {
   apiId: number;
   seasonNumber: number;
   episodeNumber: number;
   name: string;
   overview: string;
   voteAverage: number;
   voteCount: number;
   stillPath: string;
   username: string;
   seen: Seen;
   vote: number;
}
