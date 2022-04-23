import useCancelablePromises from "./useCancelablePromises";
import {cancelablePromise, delay} from "./cancelablePromise";

export default function useClickPreventionOnDoubleClick (onClick: () => void, onDoubleClick: () => void) {
   const api = useCancelablePromises();
   
   const handleClick = () => {
      api.clearPendingPromises();
      const waitForClick = cancelablePromise(delay(300));
      api.appendPendingPromise(waitForClick);
      
      return waitForClick.promise
         .then(() => {
            api.removePendingPromise(waitForClick);
            onClick();
         })
         .catch(errorInfo => {
            api.removePendingPromise(waitForClick);
            if (!errorInfo.isCanceled) {
               throw errorInfo.error;
            }
         });
   };
   
   const handleDoubleClick = () => {
      api.clearPendingPromises();
      onDoubleClick();
   };
   
   return [handleClick, handleDoubleClick];
};
