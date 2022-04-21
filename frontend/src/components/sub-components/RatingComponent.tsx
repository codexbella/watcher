import ratingStarFull from "../../images/rating-star-full.png";
import ratingStarEmpty from "../../images/rating-star-empty.png";
import {useTranslation} from "react-i18next";

interface RatingComponentProps {
   rating: number;
   onRating: (rating: number) => void;
}

export default function RatingComponent(props: RatingComponentProps) {
   const {t} = useTranslation();
   
   const rate = (rating: number) => {
      props.onRating(rating);
   }
   
   return <div className='flex nowrap margin-b15px tooltip'>
      <span className='tooltiptext background-light color-lighter padding-5px text-center margin-l-46px'>
         {t('rating-tooltip')}
      </span>
      {[...new Array(5)].map((arr, index) => {
         return index < props.rating ?
            <img src={ratingStarFull} height='18' alt='full star' onClick={() => rate(index+1)} className='pointer' key={index}/>
            :
            <img src={ratingStarEmpty} height='18' alt='empty star' onClick={() => rate(index+1)} className='pointer' key={index}/>
      })}
   </div>
}
