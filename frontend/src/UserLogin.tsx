import {useTranslation} from "react-i18next";
import {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "./auth/AuthProvider";

export default function UserLogin() {
   const [usernameField, setUsernameField] = useState('');
   const [passwordField, setPasswordField] = useState('');
   const [error, setError] = useState('');
   const {t} = useTranslation();
   const nav = useNavigate();
   
   const auth = useAuth()
   
   useEffect(() => {
      if (localStorage.getItem('jwt-token')) {
         nav('/search')
      }
   }, [nav])
   
   const login = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault()
         auth.login(usernameField, passwordField)
            .then(() => nav('/users/'+usernameField))
            .catch(e => setError(e.message))
   }
   
   return <div className='color-lighter'>
      <form onSubmit={ev => login(ev)} className='margin-bottom'>
         <input type='text' placeholder={t('username')} value={usernameField} onChange={ev => {
            setUsernameField(ev.target.value)
         }}/>
         <input type='password' placeholder={t('password')} value={passwordField} onChange={ev => {
            setPasswordField(ev.target.value)
         }}/>
         <button type='submit'>{t('login')}</button>
      </form>
      {error && <div className='color-lighter margin-bottom'>{error}.</div>}
   </div>
}
