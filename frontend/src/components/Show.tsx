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
import {useNavigate} from "react-router-dom";

interface ShowsProps {
   show: ShowData;
   onChange: () => void;
}

export default function Show(props: ShowsProps) {
   const {t} = useTranslation();
   const nav = useNavigate();
   const vote = props.show.vote / 2;
   
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
      fetch(`${process.env.REACT_APP_BASE_URL}/deleteshow/${props.show.apiId}`, {
         method: 'DELETE',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {if (response.status >= 200 && response.status < 300) {props.onChange()}})
   }
   
   return <div className='border-dark shadow height-231px width-500px flex row'>
      
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}
           onClick={() => nav('/shows/'+props.show.id)} className='pointer'/>
      
      <div className='color-lighter flex result-details wrap column'>
         <div className='flex justify-space-between'>
            <div className='margin-bottom-15px'>
               <div className='large bold small-caps overflow-1'>{props.show.name}</div>
               <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
            </div>
            <div className='flex column gap-10px text-center'>
               <div onClick={() => {if (window.confirm(`${t('sure-of-deletion')}?`)) {deleteShow()}}}
                    className='pointer'>
                  <img src={deleteSymbol} width='20' alt='delete'/>
               </div>
               <div><img src={determineEyeSource(props.show.seen)} width='33' alt='seen status'/></div>
            </div>
         </div>
         
         <div className='margin-bottom-15px'>{props.show.seasons.length - 1} {t('seasons')}</div>

         <div className='margin-bottom-15px'>
            <img src={vote >= 0.5 ? (vote >= 1 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='1'/>
            <img src={vote >= 1.5 ? (vote >= 2 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='2'/>
            <img src={vote >= 2.5 ? (vote >= 3 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='3'/>
            <img src={vote >= 3.5 ? (vote >= 4 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='4'/>
            <img src={vote >= 4.5 ? (vote >= 5 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='5'/>
         </div>
         <div className='flex gap-10px align-center'>
            <div className='border-dark color-lighter text-center height-18px width-150px'>
               <div className='background-dark height-18px' style={{width: `${props.show.voteAverage * 10}%`}}>{props.show.voteAverage}</div>
            </div>
            <div>{props.show.voteCount} {t('votes')}</div>
         </div>
      </div>
   </div>
}