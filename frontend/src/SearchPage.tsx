import './App.css';
import {useTranslation} from 'react-i18next';
import {FormEvent, useEffect, useState} from "react";
import {ShowSearchData} from "./models/ShowData";
import SearchResult from "./components/SearchResult";
import {useNavigate} from "react-router-dom";

export default function SearchPage() {
   const {t} = useTranslation();
   const [error, setError] = useState('');
   const [searchTerm, setSearchTerm] = useState('');
   const [searched, setSearched] = useState(false);
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
      setSearched(true)
      if (searchTerm !== '') {
         fetch(`${process.env.REACT_APP_BASE_URL}/search/${searchTerm}?language=${localStorage.getItem('i18nextLng')}`, {
            method: 'GET',
            headers: {
               Authorization: `Bearer ${localStorage.getItem('jwt-token')}`,
               'Content-Type': 'application/json'
            }
         })
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  return response.json();
               }
               throw new Error(`${t('search-request-error')}, ${t('error')}: ${response.status}`)
            })
            .then((list: Array<ShowSearchData>) => {
               setShowResults(list);
               setError('');
               setSearchedTerm(searchTerm)
            })
            .catch(e => setError(e.message))
      }
      setSearchTerm('')
   }
   
   return <div>
      <form onSubmit={ev => searchForShow(ev)} className="margin-bottom">
         <input className='color-lighter' type='text' placeholder={t('search-term')} value={searchTerm}
                onChange={typed => setSearchTerm(typed.target.value)}/>
         <button type='submit'>{t('send-search-request')}</button>
      </form>
      {searched ?
         showResults.length > 0
            ?
            <div>
               {searchedTerm &&
                  <div className="large color-light margin-bottom">
                     {showResults.length} {t('search-results-for-search-term')} "{searchedTerm}":
                  </div>
               }
            
               <div className='margin-bottom'>{showResults.map(item => <SearchResult show={item} key={item.apiId}/>)}
               </div>
            </div>
            :
            <div className="lds-ellipsis">
               <div></div>
               <div></div>
               <div></div>
               <div></div>
            </div>
         :
         <div></div>
      }
      {error && <div className='margin-bottom'>{error}.</div>}
   </div>
}
