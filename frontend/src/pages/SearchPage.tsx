import { useTranslation } from 'react-i18next';
import {useState} from "react";

export default function SearchPage() {
   const { t } = useTranslation();
   const [searchTerm, setSearchTerm] = useState('');
   
   
   
   return <div>
      <form>
         <input className='search-field' type='text' placeholder={t('search-term')} value={searchTerm}/>
         <button type='submit'>{t('send-search-request')}</button>
      </form>
   </div>
}