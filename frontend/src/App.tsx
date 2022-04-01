import './App.css';
import logo from './images/logo-light.png';
import {Outlet} from "react-router-dom";
import {useTranslation} from "react-i18next";

function App() {
    const { t } = useTranslation();
    
    return (
       <div>
          <div className="flex stretch watcher">
             <img className="logo-main" src={logo} alt=""/>
             <h1 className="color-lighter">atcher</h1>
          </div>
          <div><Outlet/></div>
       </div>
    );
}

export default App;
