import { useRef } from "react";

export default function useCancelablePromises () {
   const pendingPromises = useRef([] as Array<{promise: Promise<any>, cancel: () => void}>);
   
   const appendPendingPromise = (promise: {promise: Promise<any>, cancel: () => void}) =>
      pendingPromises.current = [...pendingPromises.current, promise];
   
   const removePendingPromise = (promise: {promise: Promise<any>, cancel: () => void}) =>
      pendingPromises.current = pendingPromises.current.filter(p => p !== promise);
   
   const clearPendingPromises = () => pendingPromises.current.map(p => p.cancel());
   
   return {
      appendPendingPromise,
      removePendingPromise,
      clearPendingPromises,
   };
};
