import '../App.css';
import starEmpty from '../images/star-empty.png';
import starFull from '../images/star-full.png';
import {SearchResult} from "../models/ShowInfo";
import {useTranslation} from "react-i18next";
import alternateImage from '../images/alt-image.png';
import {useState} from "react";
import VoteAverageComponent from "./sub-components/VoteAverageComponent";
import {useNavigate} from "react-router-dom";

interface SearchResultComponentProps {
   show: SearchResult;
}

export default function SearchResultComponent(props: SearchResultComponentProps) {
   const {t} = useTranslation();
   const nav = useNavigate();
   const [liked, setLiked] = useState(props.show.liked);
   const [error, setError] = useState('');
   
   const addShow = () => {
      setError('');
      fetch(`/saveshow/${props.show.apiId}?language=${localStorage.getItem('i18nextLng')}`, {
         method: 'PUT',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`,
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
      fetch(`/deleteshow/${props.show.apiId}`, {
         method: 'DELETE',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`,
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
   
   return <div className="border-dark shadow-darkest height-231px flex row margin-b15px">
      
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }} onClick={() => {if (liked) {
         nav('/shows/' + props.show.apiId)
           }}} className={liked ? 'pointer' : ''}/>
      
      <div className="color-lighter flex wrap column border-box width-100percent padding-l10px-r15px">
         <div className="flex justify-space-between">
            <div>
            <div onClick={() => {if (liked) {
               nav('/shows/' + props.show.apiId)
            }}}
                 className={liked ? 'large bold small-caps pointer' : 'large bold small-caps'}>
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
         
         <div className="margin-t15px margin-b15px"><p className="overflow lines4">{props.show.overview}</p></div>
         <VoteAverageComponent voteAverage={props.show.voteAverage} voteCount={props.show.voteCount}/>
      </div>
      {error && <div>{error}.</div>}
   </div>
}
