import './App.css';
import watcherLogo from './images/logo-light.png';
import tmdbLogo from './images/tmdb-logo.png';
import {Outlet} from "react-router-dom";

function App() {
   
   return (
      <div className="margins-left-right margin-top">
         <div className="flex watcher">
            <img height={100} src={watcherLogo} alt=""/>
            <h1 className="color-lighter">atcher</h1>
         </div>
         <div><Outlet/></div>
         <div className="flex baseline gap center">
            <img src={tmdbLogo} alt="logo of tmdb" height={20}/>
            <div className="tmdb-sentence">This product uses the TMDB API but is not endorsed or certified by TMDB.</div>
         </div>
      </div>
   );
}

export default App;
