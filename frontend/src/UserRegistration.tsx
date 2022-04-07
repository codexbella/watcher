import {FormEvent, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";

export default function UserRegistration() {
   const [usernameField, setUsernameField] = useState('');
   const [passwordField, setPasswordField] = useState('');
   const [passwordFieldAgain, setPasswordFieldAgain] = useState('');
   const {t} = useTranslation();
   const [error, setError] = useState('');
   const nav = useNavigate();
   
   useEffect(() => {
      if (localStorage.getItem('jwt-token')) {
         nav('/search')
      }
   }, [nav])
   
   const register = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault()
      setError('');
      if (passwordField === passwordFieldAgain) {
         fetch(`${process.env.REACT_APP_BASE_URL}/users/register`, {
            method: 'POST',
            body: JSON.stringify({
               username: usernameField,
               password: passwordField,
               passwordAgain: passwordFieldAgain
            }),
            headers: {
               'Content-Type': 'application/json'
            }
         })
            .then(response => {
               if (response.status >= 200 && response.status < 300) {
                  return response.text();
               } else {
               throw new Error(`${t('new-user-error')}, ${t('error-code')}: ${response.status}`)
               }
            })
            .then(() => {nav('/login')})
            .catch(e => setError(e.message))
      } else {
         setError(`${t('password-not-equal-error')}`)
      }
   }
   
   return <div className='color-lighter'>
      <form onSubmit={ev => register(ev)} className='margin-bottom'>
         <input type='text' placeholder={t('username')} value={usernameField}
                onChange={ev => setUsernameField(ev.target.value)}/>
         <input type='password' placeholder={t('password')} value={passwordField}
                onChange={ev => setPasswordField(ev.target.value)}/>
         <input type='password' placeholder={t('password-again')} value={passwordFieldAgain}
                onChange={ev => setPasswordFieldAgain(ev.target.value)}/>
         <button type='submit'>{t('register')}</button>
      </form>
      {error && <div className='color-lighter margin-bottom'>{error}.</div>}
   </div>
}
