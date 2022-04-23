import { useRef } from "react";

export default function useCancelablePromises () {
   const pendingPromises = useRef([] as Array<any>);
   
   const appendPendingPromise = (promise: Promise<any>) =>
      pendingPromises.current = [...pendingPromises.current, promise];
   
   const removePendingPromise = (promise: Promise<any>) =>
      pendingPromises.current = pendingPromises.current.filter(p => p !== promise);
   
   const clearPendingPromises = () => pendingPromises.current.map(p => p.cancel());
   
   return {
      appendPendingPromise,
      removePendingPromise,
      clearPendingPromises,
   };
};
