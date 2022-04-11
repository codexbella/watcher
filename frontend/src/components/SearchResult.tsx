import '../App.css';
import starEmpty from '../images/star-empty.png';
import starFull from '../images/star-full.png';
import {ShowSearchData} from "../models/ShowData";
import {useTranslation} from "react-i18next";
import alternateImage from '../images/alt-image.png';
import {useState} from "react";

interface SearchResultProps {
   show: ShowSearchData;
}

export default function SearchResult(props: SearchResultProps) {
   const {t} = useTranslation();
   const [liked, setLiked] = useState(props.show.liked);
   const [error, setError] = useState('');
   
   const addShow = () => {
      setError('');
      fetch(`${process.env.REACT_APP_BASE_URL}/saveshow/${props.show.apiId}?language=${localStorage.getItem('i18nextLng')}`, {
         method: 'GET',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt-token')}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               setLiked(!liked)
            } else {
               throw new Error(`${t('error')}: ${response.status}`)
            }
         })
         .catch(e => setError(e.message))
   }
   
   const deleteShow = () => {
      setError('');
      fetch(`${process.env.REACT_APP_BASE_URL}/deleteshow/${props.show.apiId}`, {
         method: 'DELETE',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt-token')}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               setLiked(!liked)
            } else if (response.status === 400) {
               window.alert(`${t('show-already-saved')}?`)
            } else {
               throw new Error(`${t('error')}: ${response.status}`)
            }
         })
         .catch(e => setError(e.message))
   }
   
   return <div className="border shadow height-231 flex row margin-bottom">
      
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}/>
      
      <div className="color-lighter flex result-details">
         <div className="flex space-between">
            <div>
            <div className="large bold small-caps">
               {props.show.name}
            </div>
            <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
            </div>
            <div className='color-lighter pointer' onClick={() => {
               if (!liked) {addShow()} else {if (window.confirm(`${t('sure-of-deletion')}?`)) {deleteShow()}}
            }}>
               {liked ? <img src={starFull} height='35' alt='unlike'/> : <img src={starEmpty} height='35' alt='like'/>}
            </div>
         </div>
         
         <div className="margin-top"><p className="overflow">{props.show.overview}</p></div>
         <div className="margin-top">{t('vote-average')}: {props.show.voteAverage} ({props.show.voteCount} {t('votes')})</div>
      </div>
      {error && <div>{error}.</div>}
   </div>
}
