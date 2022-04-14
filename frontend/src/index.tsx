import React, {Suspense} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import App from './App';
import reportWebVitals from './reportWebVitals';
import SearchPage from "./SearchPage";
import UserLogin from "./UserLogin";
import UserRegistration from "./UserRegistration";
import './i18n';
import AuthProvider from "./auth/AuthProvider";
import Watcherlist from "./Watcherlist";
import ShowDetailsPage from "./ShowDetailsPage";

ReactDOM.render(
   <React.StrictMode>
      <Suspense fallback="Loading...">
         <BrowserRouter>
            <AuthProvider>
               <Routes>
                  <Route path='/' element={<App/>}>
                     <Route path='register' element={<UserRegistration/>}/>
                     <Route path='login' element={<UserLogin/>}/>
                     <Route path='search' element={<SearchPage/>}/>
                     <Route path='watcherlist' element={<Watcherlist/>}/>
                     <Route path='shows/:id' element={<ShowDetailsPage/>}/>
                     <Route path='*' element={<SearchPage/>}/>
                  </Route>
               </Routes>
            </AuthProvider>
         </BrowserRouter>
      </Suspense>
   </React.StrictMode>,
   document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
