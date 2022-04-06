import {ShowData} from "../models/ShowData";
import {useTranslation} from "react-i18next";
import {NavLink} from "react-router-dom";
import alternateImage from "../images/alt-image.png";
import {useEffect, useState} from "react";

interface ShowResultProps {
   id: number
}

export default function ShowResult(props: ShowResultProps) {
   const {t} = useTranslation();
   const [error, setError] = useState('');
   const [show, setShow] = useState({} as ShowData);
   
   useEffect(() => {
      fetch(`${process.env.REACT_APP_BASE_URL}/getshow/${props.id}?language=${localStorage.getItem('i18nextLng')}`, {
         method: 'GET',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt-token')}`,
            'Content-Type': 'application/json'
         }})
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               return response.json();
            }
            throw new Error(`${t('get-show-request-error')}, ${t('error')}: ${response.status}`)
         })
         .then((show: ShowData) => {setShow(show); setError('')})
         .catch(e => setError(e.message))
   }, [])
   
   return <div className="border shadow height-231 flex row margin-bottom">
      <NavLink to=''>
         <img src={"https://image.tmdb.org/t/p/w154" + show.posterPath} alt={show.name}
              onError={(ev) => {
                 ev.currentTarget.onerror = null;
                 ev.currentTarget.src = alternateImage
              }}/>
      </NavLink>
      
      <div className="color-lighter flex result-details">
         <div className="">
            <div className="large bold small-caps">{show.name}</div>
            <div>{new Date(show.airDate).getFullYear()}</div>
         </div>
         <div className="margin-top"><p className="overflow">{show.overview}</p></div>
         <div className="margin-top">{t('vote-average')}: {show.voteAverage} ({show.voteCount} {t('votes')})</div>
      </div>
   
   </div>
}