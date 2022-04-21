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
      <div className="margins-l0px-r15px margin-t15px">
         <div className="margin-b15px">
            <button onClick={loginOrLogout} className='color-lighter large'>
               {auth.token? t('logout') : t('login')}
            </button>
            {!auth.token &&
               <button onClick={() => nav('/register')} className='color-lighter large'>
                  {t('register')}
               </button>}
            <button onClick={() => nav('/search')} className='color-lighter large'>
               {t('search')}
            </button>
            <button onClick={() => {nav('/watcherlist');}} className='color-lighter large'>
               Watcherlist
            </button>
         </div>
         <div className="flex row align-baseline">
            <img height={100} src={watcherLogo} alt=""/>
            <h1 className="color-lighter">atcher</h1>
         </div>
         <div><Outlet/></div>
         
         <div className="flex row align-baseline gap20px justify-center">
            <img src={tmdbLogo} alt="logo of tmdb" height={20}/>
            <div className="tmdb-sentence">{t('tmdb-sentence')}.</div>
         </div>
      </div>
   );
}

export default App;
