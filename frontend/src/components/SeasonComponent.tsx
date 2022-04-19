import {Season} from "../models/ShowInfo";
import {useState} from "react";
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import EpisodeComponent from "./EpisodeComponent";
import RatingComponent from "./sub-components/RatingComponent";
import SeenComponent from "./sub-components/SeenComponent";

interface SeasonComponentProps {
   season: Season;
   onOpen: (seasonNumber: number) => void;
   onRating: (rating: number, seasonNumber?: number, episodeNumber?: number) => void;
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
   
   return <details onClick={() => checkOpenStatus()} id={props.season.name} key={props.season.name}
                   className='border-dark shadow-darkest margin-bottom-15px padding-15px'>
      <summary className='pointer'>{props.season.name}</summary>
      <div className='flex row justify-space-between gap-20px padding-5px'>
         <div className='flex row gap-20px'>
            <img src={props.season.posterPath ? "https://image.tmdb.org/t/p/w154" + props.season.posterPath : alternateImage}
                 alt={props.season.name}
                 onError={(ev) => {
                    ev.currentTarget.onerror = null;
                    ev.currentTarget.src = alternateImage
                 }}
                 className='height-231px width-154px'/>
            <div className=''>
               {props.season.overview ? <div className='margin-bottom-15px'>{props.season.overview}</div> : <div/>}
               <div className='bold margin-bottom-15px'>{props.season.numberOfEpisodes} {t('episodes')}:</div>
               {props.season.episodes.length > 0 ?
                  <div className='margin-bottom-15px'>
                     {props.season.episodes.map(episode => <EpisodeComponent episode={episode} onRating={rateSeason}
                           key={episode.episodeNumber}/>)}
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
         
         <div className='flex column align-flex-end'>
            <RatingComponent rating={props.season.rating} onRating={rateSeason}/>
            <SeenComponent seen={props.season.seen}/>
         </div>
      </div>
   </details>
}
