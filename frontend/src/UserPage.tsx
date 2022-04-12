import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import Show from "./components/Show";
import {useCallback, useEffect, useState} from "react";
import {ShowData} from "./models/ShowInfo";

export default function UserPage() {
   const {t} = useTranslation();
   const params = useParams();
   const nav = useNavigate();
   const [error, setError] = useState('');
   const [shows, setShows] = useState([] as Array<ShowData>);
   
   const getAllShows = useCallback(() => {
         fetch(`${process.env.REACT_APP_BASE_URL}/getallshows`, {
            method: 'GET',
            headers: {
               Authorization: `Bearer ${localStorage.getItem('jwt-token')}`,
               'Content-Type': 'application/json'
            }
         })
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  return response.json();
               } else if(response.status === 401) {
                  throw new Error(`${t('logout-login')}`)
               }
               throw new Error(`${t('get-all-shows-error')}, ${t('error')}: ${response.status}`)
            })
            .then((list: Array<ShowData>) => {
               setShows(list);
               setError('');
            })
            .catch(e => setError(e.message))
   }, [t]);
   
   useEffect(() => {
      if (!localStorage.getItem('jwt-token')) {
         nav('/login')
      } else {
         getAllShows();
      }
   }, [nav, getAllShows])
   
   return <div>
      <h2 className='color-lighter margin-bottom'>{t('hello')} {params.username}!</h2>
      
      {shows &&
         <div className="large color-light margin-bottom">
            {t('you-have')} {shows.length} {t('shows-in-your-list')}:
         </div>
      }
   
      <div className='flex wrap gap margin-bottom'>{shows.map(item => <Show show={item} key={item.id}/>)}</div>
      {error && <div className='margin-bottom'>{error}.</div>}
   </div>
}