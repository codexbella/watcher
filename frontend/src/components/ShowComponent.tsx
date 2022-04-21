import {Show} from "../models/ShowInfo";
import deleteSymbol from '../images/delete.png';
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import RatingComponent from "./sub-components/RatingComponent";
import SeenComponent from "./sub-components/SeenComponent";
import {Seen} from "../Seen";

interface ShowComponentProps {
   show: Show;
   onChange: () => void;
   onRating: (id: string, rating: number) => void;
   onSeen: (id: string, seen: Seen) => void;
}

export default function ShowComponent(props: ShowComponentProps) {
   const {t} = useTranslation();
   const nav = useNavigate();
   
   const setRating = (rating: number) => {
      props.onRating(props.show.id, rating);
   }
   const setSeen = (seen: Seen) => {
      props.onSeen(props.show.id, seen);
   }
   
   const deleteShow = () => {
      fetch(`${process.env.REACT_APP_BASE_URL}/deleteshow/${props.show.apiId}`, {
         method: 'DELETE',
         headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`,
            'Content-Type': 'application/json'
         }
      })
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               props.onChange()
            }
         })
   }
   
   return <div className='border-dark shadow-darkest height-231px width-500px flex row'>
      
      <img src={props.show.posterPath ? "https://image.tmdb.org/t/p/w154" + props.show.posterPath : alternateImage} alt={props.show.name}
           onError={(ev) => {
              ev.currentTarget.onerror = null;
              ev.currentTarget.src = alternateImage
           }}
           onClick={() => nav('/shows/' + props.show.id)} className='pointer'/>
      
      <div className='color-lighter flex result-details wrap column'>
         <div className='flex justify-space-between'>
            <div className='margin-b15px'>
               <div className='large bold small-caps overflow lines1'>{props.show.name}</div>
               <div>{props.show.airDate ? new Date(props.show.airDate).getFullYear() : ''}</div>
            </div>
            <div className='flex column gap10px text-center'>
               <div onClick={() => {
                  if (window.confirm(`${t('sure-of-deletion')}?`)) {
                     deleteShow()
                  }
               }}
                    className='pointer'>
                  <img src={deleteSymbol} width='20' alt='delete'/>
               </div>
               <SeenComponent seen={props.show.seen} onSeen={setSeen}/>
            </div>
         </div>
         
         <div className='margin-b15px'>{props.show.seasons.length} {t('seasons')}</div>
         
         <div className='flex'><RatingComponent rating={props.show.rating} onRating={setRating}/>
            <div/>
         </div>
         
         <div className='flex gap10px align-center'>
            <div className='border-dark color-lighter text-center height-18px width-150px'>
               <div className='background-dark height-18px'
                    style={{width: `${props.show.voteAverage * 10}%`}}>{props.show.voteAverage}</div>
            </div>
            <div>{props.show.voteCount} {t('votes')}</div>
         </div>
      </div>
   </div>
}