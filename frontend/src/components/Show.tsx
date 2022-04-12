import {ShowData} from "../models/ShowInfo";
import starEmpty from '../images/star-empty.png';
import starFull from '../images/star-full.png';
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import eyeNotSeen from '../images/eye-not-seen.png';
import eyeSeen from '../images/eye-seen.png';
import eyePartial from '../images/eye-partially-seen.png';

interface ShowsProps {
   show: ShowData;
}

export default function Show(props: ShowsProps) {
   const {t} = useTranslation();
   
   const determineEyeSource = (seen: string) => {
      if (seen === "NO") {
         return eyeNotSeen
      } else if (seen === "YES") {
         return eyeSeen
      } else {
         return eyePartial
      }
   }
   
   return <div className='border shadow height-231 width-3-images flex row margin-bottom'>
   
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}/>
   
      <div className='color-lighter flex result-details'>
         <div className='flex space-between'>
         <div className='margin-bottom'>
               <div className='large bold small-caps'>{props.show.name}</div>
               <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
         </div>
         <div className='margin-left'><img src={determineEyeSource(props.show.seen)} height='35' alt='seen status'/></div>
         </div>
   
         <div className='margin-bottom'>{props.show.seasons.length-1} {t('seasons')}</div>
         
         <div className='margin-bottom'>
            <img src={props.show.vote >= 1? starFull : starEmpty} height='15' alt='1'/>
            <img src={props.show.vote >= 2? starFull : starEmpty} height='15' alt='2'/>
            <img src={props.show.vote >= 3? starFull : starEmpty} height='15' alt='3'/>
            <img src={props.show.vote >= 4? starFull : starEmpty} height='15' alt='4'/>
            <img src={props.show.vote >= 5? starFull : starEmpty} height='15' alt='5'/>
            <img src={props.show.vote >= 6? starFull : starEmpty} height='15' alt='6'/>
            <img src={props.show.vote >= 7? starFull : starEmpty} height='15' alt='7'/>
            <img src={props.show.vote >= 8? starFull : starEmpty} height='15' alt='8'/>
            <img src={props.show.vote >= 9? starFull : starEmpty} height='15' alt='9'/>
            <img src={props.show.vote === 10? starFull : starEmpty} height='15' alt='10'/>
         </div>
         <div>
            <img src={Math.round(props.show.voteAverage) >= 1? starFull : starEmpty} height='15' alt='1'/>
            <img src={Math.round(props.show.voteAverage) >= 2? starFull : starEmpty} height='15' alt='2'/>
            <img src={Math.round(props.show.voteAverage) >= 3? starFull : starEmpty} height='15' alt='3'/>
            <img src={Math.round(props.show.voteAverage) >= 4? starFull : starEmpty} height='15' alt='4'/>
            <img src={Math.round(props.show.voteAverage) >= 5? starFull : starEmpty} height='15' alt='5'/>
            <img src={Math.round(props.show.voteAverage) >= 6? starFull : starEmpty} height='15' alt='6'/>
            <img src={Math.round(props.show.voteAverage) >= 7? starFull : starEmpty} height='15' alt='7'/>
            <img src={Math.round(props.show.voteAverage) >= 8? starFull : starEmpty} height='15' alt='8'/>
            <img src={Math.round(props.show.voteAverage) >= 9? starFull : starEmpty} height='15' alt='9'/>
            <img src={Math.round(props.show.voteAverage) === 10? starFull : starEmpty} height='15' alt='10'/>
         </div>
         <div>({props.show.voteCount} {t('votes')})</div>
      </div>
   </div>
}