import {Season} from "../models/ShowInfo";
import {useState} from "react";

interface SeasonProps {
   season: Season;
   seasonInfo: boolean;
   onOpen: (apiId: string, index: number) => void;
}

export default function SeasonDetails(props: SeasonProps) {
   const [open, setOpen] = useState(false);
   
   const checkOpenStatus = () => {
      if (!open) {
         props.onOpen
      }
      setOpen(!open);
   }
   
   return <details onClick={() => checkOpenStatus()} id={props.season.seasonName} key={props.season.seasonName} className='border-dark shadow margin-bottom padding-15'>
      <summary className='pointer'>{props.season.seasonName}</summary>
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