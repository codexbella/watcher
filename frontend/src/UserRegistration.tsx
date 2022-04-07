import {FormEvent, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";

export default function UserRegistration() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const [usernameField, setUsernameField] = useState('');
   const [passwordField, setPasswordField] = useState('');
   const [passwordFieldAgain, setPasswordFieldAgain] = useState('');
   const [error, setError] = useState('');
   const [statusCode, setStatusCode] = useState(500);
   
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
               setStatusCode(response.status);
               return response.text()
            })
            .then(text => {
               if (statusCode >= 200 && statusCode < 300) {
                  nav('/login')
               } else {
                  setError(`${t('new-user-error')}, ${t('error-code')}: ${statusCode}, ${text}`)
               }
            })
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
