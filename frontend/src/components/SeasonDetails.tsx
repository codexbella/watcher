import {Season} from "../models/ShowInfo";
import {useState} from "react";

interface SeasonProps {
   season: Season;
   seasonInfo: boolean;
   onOpen: (apiId: number, index: number) => void;
}

export default function SeasonDetails(props: SeasonProps) {
   const [open, setOpen] = useState(false);
   
   const checkOpenStatus = () => {
      if (!open) {
         props.onOpen(1855, 1)
      }
      setOpen(!open);
   }
   
   return <details onClick={() => checkOpenStatus()} id={props.season.seasonName} key={props.season.seasonName} className='border-dark shadow margin-bottom-15px padding-15px'>
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