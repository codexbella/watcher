import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import ShowComponent from "../components/ShowComponent";
import {useCallback, useEffect, useState} from "react";
import {Show} from "../models/ShowInfo";
import {useAuth} from "../auth/AuthProvider";
import {Seen} from "../Seen";

export default function Watcherlist() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const auth = useAuth()
   const [error, setError] = useState('');
   const [shows, setShows] = useState([] as Array<Show>);
   const [sortParam, setSortParam] = useState();
   const [showsSorted, setShowsSorted] = useState([] as Array<Show>);
   const [gotShows, setGotShows] = useState(false);
   
   const editShow = (url: string, index: number) => {
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
            const showsAfter = [...shows];
            showsAfter[index] = responseBody;
            setShows(showsAfter);
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   }
   
   const determineRateUrl = (showId: string, index: number, rating: number) => {
      editShow(`${process.env.REACT_APP_BASE_URL}/editshow/${showId}?rating=${rating}`, index);
   }
   const determineSeenUrl = (showId: string, index: number, seen: Seen) => {
      editShow(`${process.env.REACT_APP_BASE_URL}/editshow/${showId}?seen=${seen}`, index);
   }
   
   const getAllShows = useCallback(() => {
      setGotShows(false);
      fetch(`${process.env.REACT_APP_BASE_URL}/getallshows`, {
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
            throw new Error(`${t('get-all-shows-error')}, ${t('error')}: ${response.status}`)
            }
         })
         .then((list: Array<Show>) => {
            setGotShows(true)
            setShows(list);
            setError('');
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   }, [nav, t]);
   
   useEffect(() => {
         getAllShows();
   }, [getAllShows])
   
   const sortShows = () => {
      setShowsSorted(shows.sort())
   }
   
   return <div>
      <div className='color-lighter margin-bottom-15px larger'>{t('hello')} {auth.username ?? t('there')}!</div>
      
      {gotShows ?
         <div>
            <div className='flex justify-space-between color-light '>
            <div className='large margin-bottom-15px'>
               {t('you-have')} {shows.length} {t('shows-in-your-list')}:
            </div>
            <form onSubmit={getAllShows}>
               <label htmlFor='sort-by' className='large'>{t('sort-by')}: </label>
               <select onSubmit={getAllShows} id='sort-by' className='background-dark medium color-lighter border-dark'>
                  <option value='notSeen'>{t('not-seen')}</option>
                  <option value='rating'>{t('own-rating')}</option>
                  <option value='vote'>{t('vote-average')}</option>
                  <option value='voteCount'>{t('vote-count')}</option>
                  <option value='inProduction'>{t('in-production')}</option>
                  <option value='airDate'>{t('airdate')}</option>
                  <option value='name'>{t('name')}</option>
                  <option value='added' selected>{t('added')}</option>
               </select>
            </form>
         </div>
            <div className='flex wrap gap-20px margin-bottom-15px'>
               {shows.map((item, index) =>
                  <ShowComponent show={item} key={item.id} index={index} onChange={getAllShows} onRating={determineRateUrl}
                                 onSeen={determineSeenUrl}/>)}
            </div>
         </div>
         :
         !error ?
            <div className="lds-ellipsis">
               <div/>
               <div/>
               <div/>
               <div/>
            </div>
            :
            <div/>
      }
      
      {error && <div className='margin-bottom-15px'>{error}.</div>}
   </div>
}
