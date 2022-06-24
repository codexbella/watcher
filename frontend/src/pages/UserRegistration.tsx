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
      if (usernameField && passwordField && passwordField === passwordFieldAgain) {
         fetch("/api/users/register", {
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
      } else if (passwordField !== passwordFieldAgain) {
         setError(`${t('password-not-equal-error')}`)
      } else if (!usernameField || !passwordField) {
         setError(`${t('fields-cannot-be-empty')}`)
      }
   }
   
   return <div className='color-lighter'>
      <form onSubmit={ev => register(ev)} className='flex column align-baseline gap10px margin-b15px'>
         <input className='large' type='text' placeholder={t('username')} value={usernameField}
                onChange={ev => {
                   setError('')
                   setUsernameField(ev.target.value)
                }}/>
         <input className='large' type='password' placeholder={t('password')} value={passwordField}
                onChange={ev => {
                   setError('')
                   setPasswordField(ev.target.value)
                }}/>
         <input className='large' type='password' placeholder={t('password-again')} value={passwordFieldAgain}
                onChange={ev => {
                   setError('')
                   setPasswordFieldAgain(ev.target.value)
                }}/>
         <div className='flex gap20px'>
         <button className='large' type='submit'>{t('register')}</button>
            <div>
               <select id='select-lang' className='background-dark large color-lighter border-dark'
                       onSelectCapture={ev => localStorage.setItem('i18nextLng', ev.currentTarget.value)}>
                  <option value='en-US'>🇺🇸</option>
                  <option value='de-DE'>🇩🇪</option>
               </select>
            </div>
         </div>
      </form>
      {error && <div className='color-lighter margin-b15px'>{error}.</div>}
   </div>
}
