import ratingStarFull from "../../images/rating-star-full.png";
import ratingStarEmpty from "../../images/rating-star-empty.png";
import {useTranslation} from "react-i18next";
import useClickPreventionOnDoubleClick from "../../double-click/useClickPreventionOnDoubleClick";

interface ClickableStarProps {
   onClick: () => void;
   onDoubleClick: () => void;
}

const ClickableFullStar = (props: ClickableStarProps) => {
   const [handleClick, handleDoubleClick] = useClickPreventionOnDoubleClick(props.onClick, props.onDoubleClick);
   
   return <img src={ratingStarFull} height='18' alt='full star' onClick={handleClick} onDoubleClick={handleDoubleClick}
        className='pointer'/>
};
const ClickableEmptyStar = (props: ClickableStarProps) => {
   const [handleClick, handleDoubleClick] = useClickPreventionOnDoubleClick(props.onClick, props.onDoubleClick);
   
   return <img src={ratingStarEmpty} height='18' alt='empty star' onClick={handleClick} onDoubleClick={handleDoubleClick}
               className='pointer'/>
};

interface RatingComponentProps {
   rating: number;
   onRating: (rating: number) => void;
}

export default function RatingComponent(props: RatingComponentProps) {
   const {t} = useTranslation();
   
   const rate = (rating: number) => {
      props.onRating(rating);
   }
   const unrate = () => {
      props.onRating(0);
   }
   
   return <div className='flex nowrap margin-b15px tooltip'>
      <span className='tooltiptext background-light color-lighter padding-5px text-center margin-l-46px pos-abs'>
         {t('rating-tooltip')}
      </span>
      {[...new Array(5)].map((arr, index) => {
         return index < props.rating ?
            <ClickableFullStar key={index} onClick={() => rate(index+1)} onDoubleClick={unrate}/>
            :
            <ClickableEmptyStar key={index} onClick={() => rate(index+1)} onDoubleClick={unrate}/>
      })}
   </div>
}
