import './App.css';
import watcherLogo from './images/logo-light.png';
import tmdbLogo from './images/tmdb-logo.png';
import {Outlet, useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {useAuth} from "./auth/AuthProvider";

function App() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const auth = useAuth()
   
   const loginOrLogout = () => {
      auth.logout();
      nav('/login');
   }
   
   return (
      <div className="margins-left-right margin-top">
         <div className="margin-bottom">
            <button onClick={loginOrLogout} className='no-decoration-text color-lighter large'>
               {auth.token? t('logout') : t('login')}
            </button>
            {!auth.token &&
               <button onClick={() => nav('/register')} className='no-decoration-text color-lighter large'>
                  {t('register')}
               </button>}
            <button onClick={() => nav('/search')} className='no-decoration-text color-lighter large'>
               {t('search')}
            </button>
            <button onClick={() => {nav('/shows/watcherlist');}} className='no-decoration-text color-lighter large'>
               Watcherlist
            </button>
         </div>
         <div className="flex row watcher">
            <img height={100} src={watcherLogo} alt=""/>
            <h1 className="color-lighter">atcher</h1>
         </div>
         <div><Outlet/></div>
         
         <div className="flex row baseline gap-20 center">
            <img src={tmdbLogo} alt="logo of tmdb" height={20}/>
            <div className="tmdb-sentence">{t('tmdb-sentence')}.</div>
         </div>
      </div>
   );
}

export default App;
