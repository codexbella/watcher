import '../App.css';
import {ShowSearchData} from "../models/ShowSearchData";
import {useTranslation} from "react-i18next";

interface ShowResultProps {
   show: ShowSearchData;
}

export default function ShowResult(props: ShowResultProps) {
   const { t } = useTranslation();
   
   return <div className="flex border shadow">
      <img src={"https://image.tmdb.org/t/p/w154"+props.show.posterPath} alt={props.show.name}/>
      <div>
         <p className="large bold small-caps padding color-lighter">{ props.show.name }</p>
         <ul className="color-lighter">
            <li>{ props.show.overview }</li>
            <li>{t('airdate')}: { props.show.airDate }</li>
            <li>{t('language')}: { props.show.language }</li>
            <li>{t('vote-average')}: { props.show.voteAverage }</li>
            <li>{t('vote-count')}: { props.show.voteCount }</li>
         </ul>
      </div>
   </div>
}
