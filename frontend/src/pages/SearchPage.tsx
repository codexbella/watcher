import '../App.css';
import {useTranslation} from 'react-i18next';
import {FormEvent, useState} from "react";
import {SearchResult} from "../models/ShowInfo";
import SearchResultComponent from "../components/SearchResultComponent";
import {useNavigate} from "react-router-dom";

export default function SearchPage() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const [error, setError] = useState('');
   const [searchTerm, setSearchTerm] = useState('');
   const [searched, setSearched] = useState(false);
   const [searchedTerm, setSearchedTerm] = useState('')
   const [showResults, setShowResults] = useState([] as Array<SearchResult>);
   
   const searchForShow = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault();
      setSearched(true)
      if (searchTerm !== '') {
            fetch(`${process.env.REACT_APP_BASE_URL}/search/${searchTerm}?language=${localStorage.getItem('i18nextLng')}`, {
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
                  nav('/login')
               } else {
                  throw new Error(`${t('search-request-error')}, ${t('error')}: ${response.status}`)
               }
            })
            .then((list: Array<SearchResult>) => {
               setShowResults(list);
               setError('');
               setSearchedTerm(searchTerm)
            })
            .catch(e => setError(e.message))
      }
      setSearchTerm('')
   }
   
   return <div>
      <form onSubmit={ev => {
         searchForShow(ev);
         setShowResults([])
      }} className="margin-bottom-15px">
         <input className='color-lighter large' type='text' placeholder={t('search-term')} value={searchTerm}
                onChange={typed => setSearchTerm(typed.target.value)}/>
         <button className='large' type='submit'>{t('send-search-request')}</button>
      </form>
      {searched ?
         showResults
            ?
            <div>
               {searchedTerm &&
                  <div className="large color-light margin-bottom-15px">
                     {showResults.length} {t('search-results-for-search-term')} "{searchedTerm}":
                  </div>
               }
               
               <div className='margin-bottom-15px'>{showResults.map(item => <SearchResultComponent show={item} key={item.apiId}/>)}
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
         :
         <div/>
      }
      {error && <div className='margin-bottom-15px'>{error}.</div>}
   </div>
}
