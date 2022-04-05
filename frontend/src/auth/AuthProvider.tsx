import {ReactNode, useContext, useEffect, useState} from "react";
import AuthContext from "./AuthContext";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

export default function AuthProvider({children}:{children :ReactNode}) {
   const {t} = useTranslation();
   
   const [token , setToken] = useState(localStorage.getItem('user-token') ?? '')
   const nav = useNavigate()
   
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
               return response.text();
            }
            throw new Error(`${t('login-error')}, ${t('error-code')}: ${response.status}`)
         })
         .then(responseToken => {
            localStorage.setItem('user-token', responseToken)
            setToken(responseToken);
         })
   }
   
   const logout = () => {
      localStorage.setItem('user-token', '')
      setToken('')
   }
   
   return <AuthContext.Provider value={{token, login, logout}} >{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext)
