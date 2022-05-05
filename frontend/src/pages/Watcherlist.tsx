import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import ShowComponent from "../components/ShowComponent";
import {useCallback, useEffect, useState} from "react";
import {Show} from "../models/ShowInfo";
import {useAuth} from "../auth/AuthProvider";
import {Seen} from "../Seen";
import {
   airDateComparator,
   inProductionComparator, lastAddedComparator, nameComparator,
   notSeenComparator,
   ratingComparator,
   voteAverageComparator,
   voteCountComparator
} from "../functions/CompareFunctions";

export default function Watcherlist() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const auth = useAuth()
   const [error, setError] = useState('');
   const [showsFromBackend, setShowsFromBackend] = useState([] as Array<Show>);
   const [showsSorted, setShowsSorted] = useState([] as Array<Show>);
   const [sortBy, setSortBy] = useState(localStorage.getItem('sort-by') ?? 'last-added');
   const [gotShows, setGotShows] = useState(false);
   const [searchTerm, setSearchTerm] = useState('');
   
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
            const showsAfter = [...showsFromBackend];
            showsAfter[showsAfter.findIndex(show => show.id === showId)] = responseBody;
            setShowsFromBackend(showsAfter);
            setShowsSorted(sortShows(sortBy, [...showsAfter]))
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
   
   const sortShows = useCallback((selected: string, shows: Show[] = [...showsFromBackend]) => {
      localStorage.setItem('sort-by', selected);
      shows.sort(lastAddedComparator);
      if (selected === 'notSeen') {
         shows.sort(notSeenComparator);
      } else if (selected === 'rating') {
         shows.sort(ratingComparator);
      } else if (selected === 'vote') {
         shows.sort(voteAverageComparator);
      } else if (selected === 'voteCount') {
         shows.sort(voteCountComparator);
      } else if (selected === 'inProduction') {
         shows.sort(inProductionComparator);
      } else if (selected === 'airDate') {
         shows.sort(airDateComparator);
      } else if (selected === 'name') {
         shows.sort(nameComparator);
      }
      setShowsSorted(shows);
      return shows;
   }, [showsFromBackend])
   
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
            setGotShows(true);
            setShowsFromBackend(list);
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
   
   useEffect(() => {
      setShowsSorted(sortShows(sortBy))
   }, [sortShows, sortBy])
   
   const getMatchingItems = (input: string) => {
      if (input === '') {
         setSearchTerm('');
         return getAllShows();
      } else {
         fetch(`${process.env.REACT_APP_BASE_URL}/getmatchingshows?searchterm=${input}`, {
            method: 'GET',
            headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}
         })
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  return response.json();
               }
               throw new Error(`${t('get-matching-error')}, ${t('error')}: ${response.status}`)
            })
            .then((list: Array<Show>) => {
               setGotShows(true);
               setShowsFromBackend(list);
               setError('');
            })
            .catch(e => setError(e.message))
      }
      setSearchTerm(input)
   }
   
   return <div>
      <div className='color-lighter margin-b15px larger'>{t('hello')} {auth.username ?? t('there')}!</div>
      
      {gotShows ?
         <div>
            <div className='flex justify-space-between color-light '>
               {searchTerm ?
                  <div className='large margin-b15px'>
                     {t('you-have')} {showsFromBackend.length} {t('shows')} {t('for-searchterm')} '{searchTerm}':
                  </div>
                  :
                  <div className='large margin-b15px'>
                     {t('you-have')} {showsFromBackend.length} {t('shows-in-your-list')}:
                  </div>
               }

               <div>
                  <input className='' type='text' placeholder={t('search-term')} value={searchTerm} onChange={typed => getMatchingItems(typed.target.value)}/>
               </div>
               <div>
                  <label htmlFor='select-sort' className='large'>{t('sort-by')}: </label>
                  <select id='select-sort' className='background-dark medium color-lighter border-dark'
                          onChange={ev => setSortBy(ev.currentTarget.value)} value={sortBy}>
                     <option value='notSeen'>{t('not-seen')}</option>
                     <option value='rating'>{t('own-rating')}</option>
                     <option value='vote'>{t('vote-average')}</option>
                     <option value='voteCount'>{t('vote-count')}</option>
                     <option value='inProduction'>{t('in-production')}</option>
                     <option value='airDate'>{t('airdate')}</option>
                     <option value='name'>{t('name')}</option>
                     <option value='last-added'>{t('last-added')}</option>
                  </select>
               </div>
            </div>
            <div className='flex wrap gap20px margin-b15px'>
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
      
      {error && <div className='margin-b15px'>{error}.</div>}
   </div>
}
