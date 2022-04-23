import {useTranslation} from "react-i18next";
import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthProvider";

export default function UserLogin() {
   const {t} = useTranslation();
   const nav = useNavigate();
   const auth = useAuth()
   const [usernameField, setUsernameField] = useState('');
   const [passwordField, setPasswordField] = useState('');
   const [error, setError] = useState('');
   
   const login = (event: FormEvent<HTMLFormElement>) => {
      event.preventDefault()
         auth.login(usernameField, passwordField)
            .then(() => nav('/users/'+usernameField))
            .catch(e => setError(e.message))
   }
   
   return <div className='color-lighter'>
      <form onSubmit={ev => login(ev)} className='flex column align-baseline gap10px margin-b15px'>
         <input className='large' type='text' placeholder={t('username')} value={usernameField} onChange={ev => {
            setUsernameField(ev.target.value)
         }}/>
         <input className='large' type='password' placeholder={t('password')} value={passwordField} onChange={ev => {
            setPasswordField(ev.target.value)
         }}/>
         <button className='large padding-lr20px' type='submit'>{t('login')}</button>
      </form>
      {error && <div className='color-lighter margin-b15px'>{error}.</div>}
   </div>
}
