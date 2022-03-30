import './App.css';
import {Outlet} from "react-router-dom";
import {useTranslation} from "react-i18next";

function App() {
    const {t} = useTranslation();
    
    return (
       <div>
           <div><h1>{t('title')}</h1></div>
           <div><Outlet/></div>
       </div>
    );
}

export default App;
