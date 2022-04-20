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
   const [showsFromBackend, setShowsFromBackend] = useState([] as Array<Show>);
   const [showsSorted, setShowsSorted] = useState([] as Array<Show>);
   const [gotShows, setGotShows] = useState(false);
   
   const editShow = (url: string, showId: string) => {
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
            const showsAfter = [...showsSorted];
            showsAfter[showsAfter.findIndex((show, index) => {
               if (show.id === showId) {return index;}
            })] = responseBody;
            setShowsFromBackend(showsAfter);
            setShowsSorted(sortShows(localStorage.getItem('sort-by') ?? 'added', [...showsAfter]))
         })
         .catch(e => {
            if (e.message === '401') {
               nav('/login')
            } else {
               setError(e.message);
            }
         })
   }
   
   const determineRateUrl = (showId: string, rating: number) => {
      editShow(`${process.env.REACT_APP_BASE_URL}/editshow/${showId}?rating=${rating}`, showId);
   }
   const determineSeenUrl = (showId: string, seen: Seen) => {
      editShow(`${process.env.REACT_APP_BASE_URL}/editshow/${showId}?seen=${seen}`, showId);
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
            setShowsFromBackend(list);
            setShowsSorted(sortShows(localStorage.getItem('sort-by') ?? 'added', list));
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
   
   const sortShows = (input: string, shows: Show[] = [...showsFromBackend]) => {
      localStorage.setItem('sort-by', input);
      if (input === 'notSeen') {
         shows.sort((a, b) => {
            if (a.seen === b.seen) {
               return 0;
            } else if (a.seen === Seen.Yes && b.seen !== Seen.Yes || a.seen === Seen.Partial && b.seen === Seen.No) {
               return 1;
            } else {
               return -1;
            }
         });
      } else if (input === 'rating') {
         shows.sort((a, b) => b.rating - a.rating);
      } else if (input === 'vote') {
         shows.sort((a, b) => b.voteAverage - a.voteAverage);
      } else if (input === 'voteCount') {
         shows.sort((a, b) => b.voteCount - a.voteCount);
      } else if (input === 'inProduction') {
         shows.sort((a, b) => {
            if (a.inProduction === b.inProduction) { return 0; } else if (a.inProduction) { return -1; } else { return 1; }
         });
      } else if (input === 'airDate') {
         shows.sort((a, b) => new Date(b.airDate).valueOf() - new Date(a.airDate).valueOf());
      } else if (input === 'name') {
         shows.sort((a, b) => {
            for (let i = 0; i < a.name.length+1; i++) {
               if (a.name.charAt(i) < b.name.charAt(i)) {
                  return -1;
               } else if (a.name.charAt(i) > b.name.charAt(i)) {
                  return 1;
               }
            }
            return 0;
         });
      } else if (input === 'added') {
         setShowsSorted([...showsFromBackend]);
         return showsFromBackend;
      }
      setShowsSorted(shows);
      return shows;
   }
   
   return <div>
      <div className='color-lighter margin-bottom-15px larger'>{t('hello')} {auth.username ?? t('there')}!</div>
      
      {gotShows ?
         <div>
            <div className='flex justify-space-between color-light '>
            <div className='large margin-bottom-15px'>
               {t('you-have')} {showsFromBackend.length} {t('shows-in-your-list')}:
            </div>
               <div>
               <label htmlFor='sort-by' className='large'>{t('sort-by')}: </label>
               <select id='sort-by' className='background-dark medium color-lighter border-dark'
                       onChange={ev => sortShows(ev.currentTarget.value)} value={localStorage.getItem('sort-by') ?? 'added'}>
                  <option value='notSeen'>{t('not-seen')}</option>
                  <option value='rating'>{t('own-rating')}</option>
                  <option value='vote'>{t('vote-average')}</option>
                  <option value='voteCount'>{t('vote-count')}</option>
                  <option value='inProduction'>{t('in-production')}</option>
                  <option value='airDate'>{t('airdate')}</option>
                  <option value='name'>{t('name')}</option>
                  <option value='added'>{t('added')}</option>
               </select>
               </div>
         </div>
            <div className='flex wrap gap-20px margin-bottom-15px'>
               {showsSorted.map(item =>
                  <ShowComponent show={item} key={item.id} onChange={getAllShows} onRating={determineRateUrl}
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
