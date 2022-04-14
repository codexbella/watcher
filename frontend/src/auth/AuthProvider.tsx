import {ReactNode, useContext, useEffect, useState} from "react";
import AuthContext from "./AuthContext";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";

export default function AuthProvider({children}:{children :ReactNode}) {
   const {t} = useTranslation();
   const nav = useNavigate();
   const [token , setToken] = useState(localStorage.getItem('jwt') ?? '')
   const [username, setUsername] = useState(t('there') as string);
   
   useEffect(() => {
      if (token) {
         const tokenDetails = JSON.parse(window.atob(token.split('.')[1]));
         setUsername(tokenDetails.sub);
      } else {
         nav('/login')
      }
      }, [nav, token])
   
   const login = (username: string, password : string) => {
      return fetch(`${process.env.REACT_APP_BASE_URL}/users/login`,{
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify({'username':username, 'password':password})
      })
         .then(response => {
            if (response.status >= 200 && response.status < 300) {
               return response.text()
            } else if (response.status === 403) {
               throw new Error(`${t('bad-credentials-error')}`)
            } else {
               throw new Error(`${t('error-code')} ${response.status}`)
            }
         })
         .then(text => {
                  localStorage.setItem('jwt', text)
                  setToken(text);
         })
   }
   
   const logout = () => {
      localStorage.removeItem('jwt');
      setToken('')
   }
   
   return <AuthContext.Provider value={{token, username, login, logout}} >{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext)
