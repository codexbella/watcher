import {Seen} from "../../Seen";
import eyeNotSeen from "../../images/eye-not-seen.png";
import eyeSeen from "../../images/eye-seen.png";
import eyePartial from "../../images/eye-partially-seen.png";

interface SeenComponentProps {
   seen: Seen;
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
   
   return <div><img src={determineEyeSource(props.seen)} width='33' alt='seen status'/></div>
}
