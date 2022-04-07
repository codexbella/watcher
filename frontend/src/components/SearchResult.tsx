import '../App.css';
import starEmpty from '../images/star-empty.png';
import starFull from '../images/star-full.png';
import {ShowSearchData} from "../models/ShowData";
import {useTranslation} from "react-i18next";
import alternateImage from '../images/alt-image.png';
import {useNavigate} from "react-router-dom";
import {useState} from "react";

interface SearchResultProps {
   show: ShowSearchData;
}

export default function SearchResult(props: SearchResultProps) {
   const {t} = useTranslation();
   const nav = useNavigate();
   const [liked, setLiked] = useState(false);
   
   return <div className="border shadow height-231 flex row margin-bottom">
      
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}
           onClick={() => nav(`/search/show/${props.show.apiId}`)}
           className='pointer'/>
      
      <div className="color-lighter flex result-details">
         <div className="flex space-between">
            <div>
            <div className="large bold small-caps pointer" onClick={() => nav(`/search/show/${props.show.apiId}`)}>
               {props.show.name}
            </div>
            <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
            </div>
            <div className='color-lighter pointer' onClick={() => setLiked(!liked)}>
               {liked ? <img src={starFull} height='35' alt='unlike'/> : <img src={starEmpty} height='35' alt='like'/>}
            </div>
         </div>
         
         <div className="margin-top"><p className="overflow">{props.show.overview}</p></div>
         <div className="margin-top">{t('vote-average')}: {props.show.voteAverage} ({props.show.voteCount} {t('votes')})</div>
      </div>
   </div>
}
