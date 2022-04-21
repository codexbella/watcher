import {Seen} from "../../Seen";
import eyeNotSeen from "../../images/eye-not-seen.png";
import eyeSeen from "../../images/eye-seen.png";
import eyePartial from "../../images/eye-partially-seen.png";
import {useTranslation} from "react-i18next";

interface SeenComponentProps {
   seen: Seen;
   onSeen: (seen: Seen) => void;
}

export default function SeenComponent(props: SeenComponentProps) {
   const {t} = useTranslation();
   
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
      if (props.seen === Seen.Yes) {
         props.onSeen(Seen.No);
      } else {
         props.onSeen(Seen.Yes);
      }
   }
   
   return <div className='pointer tooltip'>
      <span className='tooltiptext background-light color-lighter padding-5px text-center margin-left--30px'>
         {t('seen-tooltip')}
      </span>
      <img onClick={determineNextSeenStatus} src={determineEyeSource(props.seen)} width='33' alt='seen status'/>
   </div>
}
