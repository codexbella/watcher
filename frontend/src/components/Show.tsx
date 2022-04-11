import {ShowData} from "../models/ShowInfo";
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";

interface ShowsProps {
   show: ShowData;
}

export default function Show(props: ShowsProps) {
   const {t} = useTranslation();
   
   return <div className="border shadow height-231 flex row margin-bottom">
   
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}/>
   
      <div className="color-lighter flex result-details">
         <div>
               <div className="large bold small-caps">
                  {props.show.name}{props.show.tagline !== '' ?? <div> - {props.show.tagline}</div>}
               </div>
               <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
         </div>
      
         <div className="margin-top"><p className="overflow">{props.show.overview}</p></div>
         <div className="margin-top">{t('vote-average')}: {props.show.voteAverage} ({props.show.voteCount} {t('votes')})</div>
         <div className='margin-bottom'>{props.show.seasons.map(item => item.seasonName)}</div>
         <div>{t('your-vote')}: {props.show.vote}</div>
         <div>{props.show.seen}</div>
         <div>{props.show.originCountry}</div>
         <div>{props.show.languages}</div>
         <div>{props.show.genres.map(item => item.name)}</div>
      </div>
   </div>
}