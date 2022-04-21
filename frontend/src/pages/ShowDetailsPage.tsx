import {Season, Show} from "../models/ShowInfo";
import deleteSymbol from '../images/delete.png';
import alternateImage from "../images/alt-image.png";
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import SeasonComponent from "../components/SeasonComponent";
import RatingComponent from "../components/sub-components/RatingComponent";
import SeenComponent from "../components/sub-components/SeenComponent";
import VoteAverageComponent from "../components/sub-components/VoteAverageComponent";
import {Seen} from "../Seen";

export default function ShowDetailsPage() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const params = useParams();
   const [show, setShow] = useState({} as Show);
   const [error, setError] = useState('');
   const [seasonsReverse, setSeasonsReverse] = useState([] as Array<Season>)
   
   useEffect(() => {
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
         .then((responseBody: Show) => {
            setShow(responseBody);
            setSeasonsReverse([...responseBody.seasons].reverse());
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   }, [nav, t, params.id])
   
   const getSeason = (seasonNumber: number) => {
      setError('');
      fetch(`${process.env.REACT_APP_BASE_URL}/saveseason/${show.apiId}?seasonNumber=${seasonNumber}`
         +`&language=${localStorage.getItem('i18nextLng')}`, {
         method: 'PUT',
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
         .then(responseBody => {
            setShow(responseBody)
            setSeasonsReverse([...responseBody.seasons].reverse());
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
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
   
   const editShow = (url: string) => {
      fetch(url, {
         method: 'PUT',
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
               throw new Error(`${t('edit-show-error')}, ${t('error')}: ${response.status}`)
            }
         })
         .then(responseBody => {
            setShow(responseBody)
            setSeasonsReverse([...responseBody.seasons].reverse());
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   }
   
   const determineRatingUrl = (rating: number, seasonNumber?: number, episodeNumber?: number) => {
      let url = `${process.env.REACT_APP_BASE_URL}/editshow/${show.id}?`;
      if (episodeNumber && seasonNumber) {
         url = url + `seasonNumber=${seasonNumber}&episodeNumber=${episodeNumber}&`;
      } else if (seasonNumber) {
         url = url + `seasonNumber=${seasonNumber}&`;
      }
      editShow(url + `rating=${rating}`);
   }
   const determineSeenUrl = (seen: Seen, seasonNumber?: number, episodeNumber?: number) => {
      let url = `${process.env.REACT_APP_BASE_URL}/editshow/${show.id}?`;
      if (episodeNumber && seasonNumber) {
         url = url + `seasonNumber=${seasonNumber}&episodeNumber=${episodeNumber}&`;
      } else if (seasonNumber) {
         url = url + `seasonNumber=${seasonNumber}&`;
      }
      editShow(url + `seen=${seen}`);
   }
   
   return <div>
      {show.id &&
         <div className='margin-b15px'>
            <div className='flex row'>
               
               <img src={show.posterPath ? "https://image.tmdb.org/t/p/w154" + show.posterPath : alternateImage} alt={show.name}
                    onError={(ev) => {
                       ev.currentTarget.onerror = null;
                       ev.currentTarget.src = alternateImage
                    }}
                    className='height-231px width-154px'/>
               
               <div className='color-lighter flex wrap column border-box width-100percent padding-l10px-r15px'>
                  
                  <div className='height-100percent flex justify-space-between'>
                     
                     <div className='flex column justify-space-between'>
                        <div>
                           <div className='large bold small-caps overflow lines1'>{show.name}</div>
                           <div className='margin-t5px margin-b15px italic'>{show.tagline}</div>
                           <div className='margin-b15px'>{show.airDate ? new Date(show.airDate).getFullYear() : ''} ({show.originCountry})
                           </div>
                        </div>
                        
                        <div>
                           <div className='margin-b15px color-darker'>{show.genres.map((item, index) =>
                              <div className='background-light padding-5px display margin-inline-end-5px border-radius10px'
                                   key={index}>{item.name}</div>
                           )}</div>
   
                           <VoteAverageComponent voteAverage={show.voteAverage} voteCount={show.voteCount}/>
                        </div>
                     </div>
                     
                     <div className='flex column align-flex-end'>
                        <RatingComponent rating={show.rating} onRating={determineRatingUrl}/>
                        <div className='flex column gap10px text-center'>
                           <div onClick={() => {
                              if (window.confirm(`${t('sure-of-deletion')}?`)) {
                                 deleteShow()
                              }
                           }}
                                className='pointer'>
                              <img src={deleteSymbol} width='20' alt='delete'/>
                           </div>
                           <SeenComponent seen={show.seen} onSeen={determineSeenUrl}/>
                        </div>
                     </div>
                  </div>
               
               </div>
            </div>
            <div className='margin-t25px margin-b40px'>{show.overview}</div>
            
            <div>
               {seasonsReverse.map(season =>
                  <SeasonComponent key={season.name} season={season} onOpen={getSeason} onRating={determineRatingUrl}
                                   onSeen={determineSeenUrl}/>)}
            </div>
            
            {error && <div className='margin-b15px'>{error}.</div>}
         </div>
      }
   </div>
}
