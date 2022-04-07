import '../App.css';
import {ShowData} from "../models/ShowData";
import {useTranslation} from "react-i18next";
import {useParams} from "react-router-dom";
import alternateImage from "../images/alt-image.png";
import {useEffect, useState} from "react";

export default function ShowResult() {
   const {t} = useTranslation();
   const params = useParams();
   const [error, setError] = useState('');
   const [show, setShow] = useState({} as ShowData);
   
   useEffect(() => {
      fetch(`${process.env.REACT_APP_BASE_URL}/getshow/${params.apiId}?language=${localStorage.getItem('i18nextLng')}`, {
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
         .then((show: ShowData) => {setShow(show); setError('');console.log(show.posterPath)})
         .catch(e => setError(e.message))
   }, [params.apiId, t])
   
   return show ?
      <div className="border shadow height-231 flex row margin-bottom">
         <img src={"https://image.tmdb.org/t/p/w154"+show.posterPath} alt={show.name}
              onError={(ev) => {
                 ev.currentTarget.onerror = null;
                 ev.currentTarget.src = alternateImage
              }}/>
      
      <div className="color-lighter flex result-details">
         <div>
            <div className="large bold small-caps">{show.name}</div>
            <div>{new Date(show.airDate).getFullYear()}</div>
         </div>
         <div className="margin-top"><p className="overflow">{show.overview}</p></div>
         <div className="margin-top">{t('vote-average')}: {show.voteAverage} ({show.voteCount} {t('votes')})</div>
      </div>
      
      {error && <div className='margin-bottom'>{error}.</div>}
      </div>
   :
         <div className="lds-default">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
         </div>
}