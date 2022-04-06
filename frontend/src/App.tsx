import './App.css';
import watcherLogo from './images/logo-light.png';
import tmdbLogo from './images/tmdb-logo.png';
import {Outlet, useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {useAuth} from "./auth/AuthProvider";

function App() {
   const { t } = useTranslation();
   const nav = useNavigate();
   
   const loginOrLogout = () => {
      if (localStorage.getItem('jwt-token')) {
         localStorage.setItem('jwt-token', '');
      }
      nav('/login')
   }
   
   return (
      <div className="margins-left-right margin-top">
         <div className="flex row watcher">
            <img height={100} src={watcherLogo} alt=""/>
            <h1 className="color-lighter">atcher</h1>
         </div>
         <div className="margin-bottom">
         <button onClick={loginOrLogout} className='no-decoration-text color-lighter large'>
            {localStorage.getItem('jwt-token') ? t('logout') : t('login')}
         </button>
         {!localStorage.getItem('jwt-token') &&
            <button onClick={() => nav('/register')} className='no-decoration-text color-lighter large'>
               {t('register')}
            </button>}
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
