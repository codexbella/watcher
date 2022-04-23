import {FormEvent, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthProvider";

export default function UserRegistration() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const auth = useAuth()
   const [usernameField, setUsernameField] = useState('');
   const [passwordField, setPasswordField] = useState('');
   const [passwordFieldAgain, setPasswordFieldAgain] = useState('');
   const [error, setError] = useState('');
   
   useEffect(() => {
      if (auth.token) {
         nav('/users/'+auth.username)
      }
   }, [nav, auth.token, auth.username])
   
   const register = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault()
      setError('');
      if (passwordField === passwordFieldAgain) {
         fetch(`${process.env.REACT_APP_BASE_URL}/users/register`, {
            method: 'POST',
            body: JSON.stringify({
               username: usernameField,
               password: passwordField,
               passwordAgain: passwordFieldAgain,
               language: localStorage.getItem('i18nextLng') ?? 'en-US'
            }),
            headers: {
               'Content-Type': 'application/json'
            }
         })
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  nav('/login')
               } else {
                  return response.text();
               }
            })
            .then(errorMessage => {
               if (errorMessage === "Username "+usernameField+" already in use") {
                  throw new Error(`${t('username-in-use')}`)
               } else if (errorMessage === "Passwords mismatched") {
                  throw new Error(`${t('password-not-equal-error')}`)
               }
            })
            .catch(e => setError(e.message))
      } else {
         setError(`${t('password-not-equal-error')}`)
      }
   }
   
   return <div className='color-lighter'>
      <form onSubmit={ev => register(ev)} className='flex column align-baseline gap10px margin-b15px'>
         <input className='large' type='text' placeholder={t('username')} value={usernameField}
                onChange={ev => setUsernameField(ev.target.value)}/>
         <input className='large' type='password' placeholder={t('password')} value={passwordField}
                onChange={ev => setPasswordField(ev.target.value)}/>
         <input className='large' type='password' placeholder={t('password-again')} value={passwordFieldAgain}
                onChange={ev => setPasswordFieldAgain(ev.target.value)}/>
         <button className='large' type='submit'>{t('register')}</button>
         <div className='flex align-center gap10px very-large'>
            <span>🇺🇸</span>
         <label className="switch pos-rel">
            <input type="checkbox"/><span className="slider pos-abs pointer"></span>
         </label>
            <span>🇩🇪</span>
         </div>
      </form>
      {error && <div className='color-lighter margin-b15px'>{error}.</div>}
   </div>
}
