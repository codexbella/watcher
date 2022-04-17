import {useTranslation} from "react-i18next";

interface VoteAverageComponentProps {
   voteAverage: number;
   voteCount: number;
}

export default function VoteAverageComponent(props: VoteAverageComponentProps) {
   const {t} = useTranslation();
   
   return <div className='flex gap-10px align-center'>
      <div className='border-dark color-lighter text-center height-18px width-150px'>
         <div className='background-dark height-18px'
              style={{width: `${props.voteAverage * 10}%`}}>{props.voteAverage}</div>
      </div>
      <div>{props.voteCount} {t('votes')}</div>
   </div>
}
