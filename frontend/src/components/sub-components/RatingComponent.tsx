import ratingStarFull from "../../images/rating-star-full.png";
import ratingStarEmpty from "../../images/rating-star-empty.png";

interface RatingComponentProps {
   rating: number;
   onRating: (rating: number) => void;
}

export default function RatingComponent(props: RatingComponentProps) {
   const vote = props.rating/2;
   
   const rate = (rating: number) => {
      console.log('rating: '+rating)
      props.onRating(rating);
   }
   
   return <div className='flex nowrap margin-bottom-15px'>
      {[...new Array(5)].map((arr, index) => {
         return index < vote ?
            <img src={ratingStarFull} height='18' alt='full star' onClick={() => rate(index+1)} className='pointer' key={index}/>
            :
            <img src={ratingStarEmpty} height='18' alt='empty star' onClick={() => rate(index+1)} className='pointer' key={index}/>
      })}
   </div>
}
