import './App.css';
import { useTranslation } from 'react-i18next';
import {FormEvent, useEffect, useState} from "react";
import {ShowSearchData} from "./models/ShowSearchData";
import ShowResult from "./components/ShowResult";
import {useAuth} from "./auth/AuthProvider";
import {useNavigate} from "react-router-dom";

export default function SearchPage() {
   const { t } = useTranslation();
   const auth = useAuth()
   const [error, setError] = useState('');
   const [searchTerm, setSearchTerm] = useState('');
   const [searchedTerm, setSearchedTerm] = useState('')
   const [showResults, setShowResults] = useState([] as Array<ShowSearchData>);
   const nav = useNavigate();
   
   useEffect(() => {
      if (!localStorage.getItem('jwt-token')) {
         nav('/login')
      }
   }, [nav])
   
   const searchForShow = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault();
      if (searchTerm !== '') {
         fetch(`${process.env.REACT_APP_BASE_URL}/search/`+searchTerm, {
               method: 'GET',
               headers: {
                  Authorization: `Bearer ${auth.token}`,
                  'Content-Type': 'application/json'
               }})
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  return response.json();
               }
               throw new Error(`${t('search-request-error')}, ${t('error')}: ${response.status}`)
            })
            .then((list: Array<ShowSearchData>) => {setShowResults(list); setError('')})
            .catch(e => {console.log(e.message); setError(e.message)})
      }
      setSearchedTerm(searchTerm);
      setSearchTerm('')
   }
   
   return <div>
      <form onSubmit={ev => searchForShow(ev)} className="margin-bottom">
         <input className='color-lighter' type='text' placeholder={t('search-term')} value={searchTerm} onChange={typed => setSearchTerm(typed.target.value)}/>
         <button type='submit'>{t('send-search-request')}</button>
      </form>
      
      {searchedTerm && <div className="large color-light margin-bottom">{showResults.length} {t('search-results-for-search-term')} "{searchedTerm}":</div>}
      
      <div className='margin-bottom'>{showResults && showResults.map(item => <ShowResult show={item} key={item.apiId}/>)}</div>
      
      {error && <div className='margin-bottom'>{error}.</div>}
   </div>
}
