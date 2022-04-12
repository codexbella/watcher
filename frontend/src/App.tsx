import './App.css';
import watcherLogo from './images/logo-light.png';
import tmdbLogo from './images/tmdb-logo.png';
import {Outlet, useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";

function App() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const [username, setUsername] = useState('');
   
   const parseJwt = (token: string) => {
      try {
         return JSON.parse(window.atob(token.split('.')[1]));
      } catch (e) {
         return {'sub': ''};
      }
   };
   
   useEffect(() => {
      if (localStorage.getItem('jwt-token')) {
      const tokenDetails = parseJwt(localStorage.getItem('jwt-token') ?? '');
      if (tokenDetails.exp > Date.now()) {
         setUsername(tokenDetails.sub);
      } else {
         nav('/login')
      }
      } else {
         nav('/login')
      }
   }, [])
   
   const loginOrLogout = () => {
      localStorage.setItem('jwt-token', '');
      nav('/login')
   }
   
   return (
      <div className="margins-left-right margin-top">
         <div className="margin-bottom">
            <button onClick={loginOrLogout} className='no-decoration-text color-lighter large'>
               {localStorage.getItem('jwt-token') ? t('logout') : t('login')}
            </button>
            {!localStorage.getItem('jwt-token') &&
               <button onClick={() => nav('/register')} className='no-decoration-text color-lighter large'>
                  {t('register')}
               </button>}
            <button onClick={() => nav('/search')} className='no-decoration-text color-lighter large'>
               {t('search')}
            </button>
            <button onClick={() => {nav('/users/'+username);}} className='no-decoration-text color-lighter large'>
               {username ? username: t('userpage')}
            </button>
         </div>
         <div className="flex row watcher">
            <img height={100} src={watcherLogo} alt=""/>
            <h1 className="color-lighter">atcher</h1>
         </div>
         <div><Outlet/></div>
         
         <div className="flex row baseline gap center">
            <img src={tmdbLogo} alt="logo of tmdb" height={20}/>
            <div className="tmdb-sentence">{t('tmdb-sentence')}.</div>
         </div>
      </div>
   );
}

export default App;
