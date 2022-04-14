import {Season, ShowData} from "./models/ShowInfo";
import ratingStarEmpty from './images/rating-star-empty.png';
import ratingStarFull from './images/rating-star-full.png';
import ratingStarHalf from './images/rating-star-half.png';
import deleteSymbol from './images/delete.png';
import alternateImage from "./images/alt-image.png";
import {useTranslation} from "react-i18next";
import eyeNotSeen from './images/eye-not-seen.png';
import eyeSeen from './images/eye-seen.png';
import eyePartial from './images/eye-partially-seen.png';
import {useNavigate, useParams} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
import SeasonDetails from "./components/SeasonDetails";

export default function ShowDetailsPage() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const params = useParams();
   const [show, setShow] = useState({} as ShowData);
   const [error, setError] = useState();
   const [seasonsSimple, setSeasonsSimple] = useState([] as Array<Season>)
   const [seasons, setSeasons] = useState([] as Array<Season>)
   const [seasonInfo, setSeasonInfo] = useState([] as Array<boolean>)
   
   const getShow = useCallback(() => (
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
         .then((responseBody: ShowData) => {
            setShow(responseBody);
            setSeasonsSimple(responseBody.seasons.slice(1, responseBody.seasons.length + 1).reverse());
            
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   ), [nav, t, params.id])
   
   useEffect(() => {
      getShow();
   }, [getShow])
   
   const getSeason = (apiId: number, index: number) => {
      fetch(`${process.env.REACT_APP_BASE_URL}/getseason/${apiId}`, {
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
               throw new Error(`${t('get-season-error')}, ${t('error')}: ${response.status}`)
            }
         })
         .then((responseBody: Season) => {
            const seasonArray = seasons;
            seasons[index] = responseBody;
            setSeasons(seasonArray);
            
            const seasonInfoArray = seasonInfo;
            seasonInfoArray[index] = true;
            setSeasonInfo(seasonInfoArray);
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   }
   
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
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               nav('/watcherlist')
            }
         })
   }
   
   return <div>
      {show.id &&
         <div className='margin-bottom-15px'>
            <div className='flex row'>
               
               <img src={show.posterPath ? "https://image.tmdb.org/t/p/w154" + show.posterPath : alternateImage} alt={show.name}
                    onError={(ev) => {
                       ev.currentTarget.onerror = null;
                       ev.currentTarget.src = alternateImage
                    }}
                    className='height-231px width-154px'/>
               
               <div className='color-lighter flex result-details wrap column'>
                  
                  <div className='height-100percent flex justify-space-between'>
                     
                     <div className='flex column justify-space-between'>
                        <div>
                           <div className='large bold small-caps overflow-1'>{show.name}</div>
                           <div className='margin-top-5px margin-bottom-15px italic'>{show.tagline}</div>
                           <div className='margin-bottom-15px'>{show.airDate ? new Date(show.airDate).getFullYear() : ''} ({show.originCountry})
                           </div>
                        </div>
                        
                        <div>
                           <div className='margin-bottom-15px color-darker'>{show.genres.map((item, index) =>
                              <div className='background-light padding-5px display margin-inline-end-5px border-radius-10px'
                                   key={index}>{item.name}</div>
                           )}</div>
                           
                           <div className='flex gap-10px align-center'>
                              <div className='border-dark color-lighter text-center height-18px width-150px'>
                                 <div className='background-dark height-18px'
                                      style={{width: `${show.voteAverage * 10}%`}}>{show.voteAverage}</div>
                              </div>
                              <div>{show.voteCount} {t('votes')}</div>
                           </div>
                        </div>
                     </div>
                     
                     <div className='flex column align-flex-end'>
                        <div className='margin-bottom-15px'>
                           <img src={vote >= 0.5 ? (vote >= 1 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='1'/>
                           <img src={vote >= 1.5 ? (vote >= 2 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='2'/>
                           <img src={vote >= 2.5 ? (vote >= 3 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='3'/>
                           <img src={vote >= 3.5 ? (vote >= 4 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='4'/>
                           <img src={vote >= 4.5 ? (vote >= 5 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='5'/>
                        </div>
                        <div className='flex column gap-10px text-center'>
                           <div onClick={() => {
                              if (window.confirm(`${t('sure-of-deletion')}?`)) {
                                 deleteShow()
                              }
                           }}
                                className='pointer'>
                              <img src={deleteSymbol} width='20' alt='delete'/>
                           </div>
                           <div><img src={determineEyeSource(show.seen)} width='33' alt='seen status'/></div>
                        </div>
                     </div>
                  </div>
               
               </div>
            </div>
            <div className='margin-top-15px margin-bottom-15px'>{show.overview}</div>
            
            <div>
               {seasonsSimple.map((item, index) =>
                  <SeasonDetails season={item} seasonInfo={seasonInfo[index]} onOpen={() => getSeason(item.apiId, index)}/>)}
            </div>
            
            {error && <div className='margin-bottom-15px'>{error}.</div>}
         </div>
         
      }
   </div>
}
