import {ShowData} from "../models/ShowInfo";
import starEmpty from '../images/star-empty.png';
import starFull from '../images/star-full.png';
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import eyeNotSeen from '../images/eye-not-seen.png';
import eyeSeen from '../images/eye-seen.png';
import eyePartial from '../images/eye-partially-seen.png';
import {useAuth} from "../auth/AuthProvider";

interface ShowsProps {
   show: ShowData;
   onChange: () => void;
}

export default function Show(props: ShowsProps) {
   const {t} = useTranslation();
   const auth = useAuth()
   const vote = Math.round(props.show.vote / 2);
   
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
            Authorization: `Bearer ${auth.token}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {if (response.status >= 200 && response.status < 300) {props.onChange()}})
   }
   
   return <div className='border-dark shadow height-231 width-500px flex row'>
      
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}/>
      
      <div className='color-lighter flex result-details'>
         <div className='flex space-between'>
            <div className='margin-bottom'>
               <div className='large bold small-caps overflow-1'>{props.show.name}</div>
               <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
            </div>
            <div className='center'>
               <div onClick={() => {if (window.confirm(`${t('sure-of-deletion')}?`)) {deleteShow()}}}
                    className='pointer'>
                  <img src={starFull} width='30' alt='unlike'/>
               </div>
               <div><img src={determineEyeSource(props.show.seen)} width='38' alt='seen status'/></div>
            </div>
         </div>
         
         <div className='margin-bottom'>{props.show.seasons.length - 1} {t('seasons')}</div>
         
         <div className='margin-bottom'>
            <img src={vote >= 1 ? starFull : starEmpty} height='18' alt='1'/>
            <img src={vote >= 2 ? starFull : starEmpty} height='18' alt='2'/>
            <img src={vote >= 3 ? starFull : starEmpty} height='18' alt='3'/>
            <img src={vote >= 4 ? starFull : starEmpty} height='18' alt='4'/>
            <img src={vote >= 5 ? starFull : starEmpty} height='18' alt='5'/>
         </div>
         <div>
            <div className='border-dark color-lighter center' style={{width: '70%'}}>
               <div className='background-dark' style={{width: `${props.show.voteAverage * 10}%`}}>{props.show.voteAverage}</div>
            </div>
            <div>{props.show.voteCount} {t('votes')}</div>
         </div>
      </div>
   </div>
}