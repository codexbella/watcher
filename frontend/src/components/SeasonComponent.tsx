import {Season} from "../models/ShowInfo";
import {useState} from "react";
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import EpisodeComponent from "./EpisodeComponent";
import RatingComponent from "./sub-components/RatingComponent";
import SeenComponent from "./sub-components/SeenComponent";
import {Seen} from "../Seen";

interface SeasonComponentProps {
   season: Season;
   onOpen: (seasonNumber: number) => void;
   onRating: (rating: number, seasonNumber?: number, episodeNumber?: number) => void;
   onSeen: (seen: Seen, seasonNumber?: number, episodeNumber?: number) => void;
}

export default function SeasonComponent(props: SeasonComponentProps) {
   const {t} = useTranslation();
   const [open, setOpen] = useState(false);
   
   const checkOpenStatus = () => {
      if (!open && props.season.episodes.length === 0) {
         props.onOpen(props.season.seasonNumber)
      }
      setOpen(!open);
   }
   
   const rateSeason = (rating: number, seasonNumber?: number, episodeNumber?: number) => {
      if (episodeNumber) {
         props.onRating(rating, seasonNumber, episodeNumber);
      } else {
         props.onRating(rating, props.season.seasonNumber)
      }
   }
   const seenSeason = (seen: Seen, seasonNumber?: number, episodeNumber?: number) => {
      if (episodeNumber) {
         props.onSeen(seen, seasonNumber, episodeNumber);
      } else {
         props.onSeen(seen, props.season.seasonNumber)
      }
   }
   
   return <div className='flex justify-space-between border-dark shadow-darkest margin-b15px padding-15px'>
      <details onClick={() => checkOpenStatus()} id={props.season.name} key={props.season.name}>
      <summary className='pointer'>
         {props.season.name}
      </summary>
      <div className='flex row justify-space-between gap20px padding-5px margin-t15px'>
            <img src={props.season.posterPath ? "https://image.tmdb.org/t/p/w154" + props.season.posterPath : alternateImage}
                 alt={props.season.name}
                 onError={(ev) => {
                    ev.currentTarget.onerror = null;
                    ev.currentTarget.src = alternateImage
                 }}
                 className='height-231px width-154px'/>
            <div className=''>
               {props.season.overview ? <div className='margin-b15px'>{props.season.overview}</div> : <div/>}
               <div className='bold margin-b15px'>{props.season.numberOfEpisodes} {t('episodes')}:</div>
               {props.season.episodes.length > 0 ?
                  <div className='margin-b15px'>
                     {[...props.season.episodes].reverse().map(episode => <EpisodeComponent episode={episode} onRating={rateSeason}
                           onSeen={seenSeason} key={episode.episodeNumber}/>)}
                  </div>
               :
                  <div className="lds-ellipsis">
                     <div/>
                     <div/>
                     <div/>
                     <div/>
                  </div>
               }
            </div>
      </div>
   </details>
      <div className='flex gap20px align-baseline'>
         <RatingComponent rating={props.season.rating} onRating={rateSeason}/>
         <SeenComponent seen={props.season.seen} onSeen={seenSeason}/>
      </div>
   </div>
}
