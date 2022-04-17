import {Episode} from "../models/ShowInfo";
import alternateImage from "../images/alt-image.png";
import VoteComponent from "./sub-components/VoteComponent";
import SeenComponent from "./sub-components/SeenComponent";
import VoteAverageComponent from "./sub-components/VoteAverageComponent";

interface EpisodeComponentProps {
   episode: Episode;
}

export default function EpisodeComponent(props: EpisodeComponentProps) {
   
   return <div className='flex border-dark gap-20px justify-space-between margin-bottom-15px padding-15px shadow-dark'>
      <div className='flex column gap-5px width-100percent'>
         <div className='flex gap-20px justify-space-between'>
            <div className='flex gap-20px width-100percent'>
               <div className='flex align-flex-start justify-center color-dark bold very-large width-70px font-courier text-shadow'>
                  {props.episode.episodeNumber}
               </div>
               <div className='flex width-100percent align-flex-start justify-space-between'>
                  <div>
                     <div className='small-caps margin-bottom-5px'>{props.episode.name}</div>
                     {props.episode.airDate ?
                        <div className='margin-bottom-15px'>{new Date(props.episode.airDate).toLocaleDateString()}</div>
                        :
                        <div/>
                     }
                  </div>
                  <VoteAverageComponent voteAverage={props.episode.voteAverage} voteCount={props.episode.voteCount}/>
               </div>
            
            </div>
            
            <div className='flex column align-flex-end'>
               <VoteComponent vote={props.episode.vote}/>
               <SeenComponent seen={props.episode.seen}/>
            </div>
         </div>
         
         <div className='margin-bottom-5px'>{props.episode.overview}</div>
      </div>
      
      <img src={props.episode.stillPath ? "https://image.tmdb.org/t/p/w185" + props.episode.stillPath : alternateImage}
           alt={props.episode.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }} className='height-139px width-185px'/>
   </div>
}
