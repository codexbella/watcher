import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import Show from "./components/Show";
import {useCallback, useEffect, useState} from "react";
import {ShowData} from "./models/ShowInfo";
import {useAuth} from "./auth/AuthProvider";

export default function UserPage() {
   const {t} = useTranslation();
   const params = useParams();
   const nav = useNavigate();
   const auth = useAuth()
   const [error, setError] = useState('');
   const [shows, setShows] = useState([] as Array<ShowData>);
   const [gotShows, setGotShows] = useState(false);
   
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
               throw new Error(`${t('logout-login')}`)
            }
            throw new Error(`${t('get-all-shows-error')}, ${t('error')}: ${response.status}`)
         })
         .then((list: Array<ShowData>) => {
            setShows(list);
            setError('');
         })
         .then(() => setGotShows(true))
         .catch(e => setError(e.message))
   }, [t]);
   
   useEffect(() => {
      if (!auth.token || !auth.expiration) {
         nav('/login')
      } else {
         getAllShows();
      }
   }, [nav, getAllShows, auth.token, auth.expiration])
   
   return <div>
      <div className='color-lighter margin-bottom larger'>{t('hello')} {params.username ?? t('there')}!</div>
      
      {gotShows ?
         <div>
            <div className="large color-light margin-bottom">
               {t('you-have')} {shows.length} {t('shows-in-your-list')}:
            </div>
            <div className='flex wrap gap margin-bottom'>{shows.map(item => <Show show={item} key={item.id}/>)}</div>
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
      
      {error && <div className='margin-bottom'>{error}.</div>}
   </div>
}