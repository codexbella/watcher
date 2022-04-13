import {ShowData} from "../models/ShowInfo";
import ratingStarEmpty from '../images/rating-star-empty.png';
import ratingStarFull from '../images/rating-star-full.png';
import ratingStarHalf from '../images/rating-star-half.png';
import deleteSymbol from '../images/delete.png';
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import eyeNotSeen from '../images/eye-not-seen.png';
import eyeSeen from '../images/eye-seen.png';
import eyePartial from '../images/eye-partially-seen.png';
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";

export default function ShowDetails() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const params = useParams();
   const [show, setShow] = useState({} as ShowData);
   const [error, setError] = useState();
   
   const getShow = () => (
      fetch(`${process.env.REACT_APP_BASE_URL}/getshow/${params.id}`, {
         method: 'GET',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               return response.json();
            } else if (response.status === 401) {
               throw new Error(`${response.status}`)
            } else {
               throw new Error(`${t('get-show-error')}, ${t('error')}: ${response.status}`)
            }
         })
         .then(responseBody => setShow(responseBody))
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   )
   
   useEffect(() => {
      getShow()
   }, [getShow()])
   
   const vote = show.vote / 2;
   
   const determineEyeSource = (seen: string) => {
      if (seen === "NO") {
         return eyeNotSeen
      } else if (seen === "YES") {
         return eyeSeen
      } else {
         return eyePartial
      }
   }
   
   const deleteShow = () => {
      fetch(`${process.env.REACT_APP_BASE_URL}/deleteshow/${show.apiId}`, {
         method: 'DELETE',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {if (response.status >= 200 && response.status < 300) {nav('/shows/watcherlist')}})
   }
   
   return <div className='border-dark shadow height-231 width-500px flex row'>
      
      <img src={show.posterPath ? "https://image.tmdb.org/t/p/w154" + show.posterPath : alternateImage} alt={show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}/>
      
      <div className='color-lighter flex result-details'>
         <div className='flex space-between'>
            <div className='margin-bottom'>
               <div className='large bold small-caps overflow-1'>{show.name}</div>
               <div>{show.airDate ? new Date(show.airDate).getFullYear() : ''}</div>
            </div>
            <div className='flex column gap-10 center'>
               <div onClick={() => {if (window.confirm(`${t('sure-of-deletion')}?`)) {deleteShow()}}}
                    className='pointer'>
                  <img src={deleteSymbol} width='20' alt='delete'/>
               </div>
               <div><img src={determineEyeSource(show.seen)} width='33' alt='seen status'/></div>
            </div>
         </div>
         
         <div className='margin-bottom'>{show.seasons.length - 1} {t('seasons')}</div>

         <div className='margin-bottom'>
            <img src={vote >= 0.5 ? (vote >= 1 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='1'/>
            <img src={vote >= 1.5 ? (vote >= 2 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='2'/>
            <img src={vote >= 2.5 ? (vote >= 3 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='3'/>
            <img src={vote >= 3.5 ? (vote >= 4 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='4'/>
            <img src={vote >= 4.5 ? (vote >= 5 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='5'/>
         </div>
         <div className='flex gap-10 align-center'>
            <div className='border-dark color-lighter center height-18' style={{width: '60%'}}>
               <div className='background-dark height-18' style={{width: `${show.voteAverage * 10}%`}}>{show.voteAverage}</div>
            </div>
            <div>{show.voteCount} {t('votes')}</div>
         </div>
         {error && <div className='margin-bottom'>{error}.</div>}
      </div>
   </div>
}