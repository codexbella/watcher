import '../App.css';
import {ShowSearchData} from "../models/ShowData";
import {useTranslation} from "react-i18next";
import alternateImage from '../images/alt-image.png';
import {NavLink} from "react-router-dom";

interface SearchResultProps {
   show: ShowSearchData;
}

export default function SearchResult(props: SearchResultProps) {
   const {t} = useTranslation();
   
   return <div className="border shadow height-231 flex row margin-bottom">
      <NavLink to=''>
      <img src={"https://image.tmdb.org/t/p/w154" + props.show.posterPath} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}/>
      </NavLink>
      
      <div className="color-lighter flex result-details">
         <div className="">
            <div className="large bold small-caps">{props.show.name}</div>
            <div>{new Date(props.show.airDate).getFullYear()}</div>
         </div>
         <div className="margin-top"><p className="overflow">{props.show.overview}</p></div>
         <div className="margin-top">{t('vote-average')}: {props.show.voteAverage} ({props.show.voteCount} {t('votes')})</div>
      </div>
   
   </div>
}
