import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import ShowComponent from "../components/ShowComponent";
import {useCallback, useEffect, useState} from "react";
import {Show} from "../models/ShowInfo";
import {useAuth} from "../auth/AuthProvider";

export default function Watcherlist() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const auth = useAuth()
   const [error, setError] = useState('');
   const [shows, setShows] = useState([] as Array<Show>);
   const [gotShows, setGotShows] = useState(false);
   
   const editShow = (showId: string, rating: number) => {
      const index = shows.findIndex(show => show.id === showId);
      const showToChange = shows[index];
      showToChange.rating = rating;
      fetch(`${process.env.REACT_APP_BASE_URL}/editshow`, {
         method: 'PUT',
         body: JSON.stringify(showToChange),
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
            const showsAfter = shows;
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
   
   return <div>
      <div className='color-lighter margin-bottom-15px larger'>{t('hello')} {auth.username ?? t('there')}!</div>
      
      {gotShows ?
         <div>
            <div className="large color-light margin-bottom-15px">
               {t('you-have')} {shows.length} {t('shows-in-your-list')}:
            </div>
            <div className='flex wrap gap-20px margin-bottom-15px'>
               {shows.map(item => <ShowComponent show={item} key={item.id} onChange={getAllShows} onRating={editShow}/>)}
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
