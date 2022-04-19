import {Seen} from "../../Seen";
import eyeNotSeen from "../../images/eye-not-seen.png";
import eyeSeen from "../../images/eye-seen.png";
import eyePartial from "../../images/eye-partially-seen.png";

interface SeenComponentProps {
   seen: Seen;
   onSeen: (seen: Seen) => void;
}

export default function SeenComponent(props: SeenComponentProps) {
   const determineEyeSource = (seen: string) => {
      if (seen === "NO") {
         return eyeNotSeen
      } else if (seen === "YES") {
         return eyeSeen
      } else {
         return eyePartial
      }
   }
   const determineNextSeenStatus = () => {
      if (props.seen === Seen.No) {
         props.onSeen(Seen.Partial);
      } else if (props.seen === Seen.Partial) {
         props.onSeen(Seen.Yes);
      } else {
         props.onSeen(Seen.No);
      }
   }
   
   return <div className='pointer'>
      <img onClick={determineNextSeenStatus} src={determineEyeSource(props.seen)} width='33' alt='seen status'/>
   </div>
}
