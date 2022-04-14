import {Season} from "../models/ShowInfo";

interface SeasonProps {
   season: Season;
   seasonInfo: boolean;
   onOpen: (apiId: string, index: number) => void;
}

export default function SeasonDetails(props: SeasonProps) {
   
   return <details onClick={() => props.onOpen} id={props.season.seasonName} key={props.season.seasonName} className='border-dark shadow margin-bottom padding-15'>
      <summary>{props.season.seasonName}</summary>
      {props.seasonInfo ?
         <div>...</div>
         :
         <div className="lds-ellipsis">
            <div/>
            <div/>
            <div/>
            <div/>
         </div>}
   </details>
}