export interface ShowSearchData {
   apiId: string;
   name: string;
   overview: string;
   airDate: string;
   language: string;
   voteAverage: number;
   voteCount: number;
   posterPath: string;
}

export interface ShowData {
   apiId: string;
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
   seasons: Array<Season>
}

interface Genre {
   apiId: number;
   name: string
}
interface Season {
   apiId: number;
   seasonNumber: number;
   seasonName: string;
   numberOfEpisodes: number;
   overview: string;
   posterPath: string;
   episodes: Array<Episode>;
}
interface Episode {
   apiId: number,
   seasonNumber: number,
   episodeNumber: number;
   name: string;
   overview: string;
   voteAverage: number;
   voteCount: number;
   stillPath: string;
}
