import {Season} from "../models/ShowInfo";
import {useState} from "react";

interface SeasonComponentProps {
   season: Season;
   seasonInfo: boolean;
   onOpen: (apiId: number, seasonNumber: number) => void;
}

export default function SeasonComponent(props: SeasonComponentProps) {
   const [open, setOpen] = useState(false);
   
   const checkOpenStatus = () => {
      if (!open) {
         props.onOpen(props.season.apiId, props.season.seasonNumber)
      }
      setOpen(!open);
   }
   
   return <details onClick={() => checkOpenStatus()} id={props.season.name} key={props.season.name} className='border-dark shadow margin-bottom-15px padding-15px'>
      <summary className='pointer'>{props.season.name}</summary>
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
