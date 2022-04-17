import ratingStarFull from "../../images/rating-star-full.png";
import ratingStarHalf from "../../images/rating-star-half.png";
import ratingStarEmpty from "../../images/rating-star-empty.png";

interface VoteComponentProps {
   vote: number;
}

export default function VoteComponent(props: VoteComponentProps) {
   const vote = props.vote / 2;
   
   return <div className='flex nowrap margin-bottom-15px'>
      <img src={vote >= 0.5 ? (vote >= 1 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='1'/>
      <img src={vote >= 1.5 ? (vote >= 2 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='2'/>
      <img src={vote >= 2.5 ? (vote >= 3 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='3'/>
      <img src={vote >= 3.5 ? (vote >= 4 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='4'/>
      <img src={vote >= 4.5 ? (vote >= 5 ? ratingStarFull : ratingStarHalf) : ratingStarEmpty} height='18' alt='5'/>
   </div>
}