import { useTranslation } from 'react-i18next';
import {FormEvent, useState} from "react";

export default function SearchPage() {
   const { t } = useTranslation();
   const [error, setError] = useState('');
   const [searchTerm, setSearchTerm] = useState('');
   
   const searchForShow = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault();
      if (searchTerm !== '') {
         fetch(`${process.env.REACT_APP_BASE_URL}/search=${searchTerm}`)
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  return response.json();
               }
               throw new Error(`${t('search-request-error')}, ${t('error')}: ${response.status}`)
            })
            .then()
            .catch(e => {console.log(e.message); setError(e.message)})
      }
      setSearchTerm('')
   }
   
   return <div>
      <form onSubmit={ev => searchForShow(ev)}>
         <input className='search-field' type='text' placeholder={t('search-term')} value={searchTerm} onChange={typed => setSearchTerm(typed.target.value)}/>
         <button type='submit'>{t('send-search-request')}</button>
      </form>
   </div>
}