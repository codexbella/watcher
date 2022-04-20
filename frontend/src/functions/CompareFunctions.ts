import {Show} from "../models/ShowInfo";
import {Seen} from "../Seen";

export function notSeenComparator(a: Show, b: Show) {
   if (a.seen === b.seen) {
      return 0;
   } else if ((a.seen === Seen.Yes && b.seen !== Seen.Yes) || (a.seen === Seen.Partial && b.seen === Seen.No)) {
      return 1;
   } else {
      return -1;
   }
}
export function ratingComparator(a: Show, b: Show) {
   return b.rating - a.rating;
}
export function voteAverageComparator(a: Show, b: Show) {
   return b.voteAverage - a.voteAverage;
}
export function voteCountComparator(a: Show, b: Show) {
   return b.voteCount - a.voteCount;
}
export function inProductionComparator(a: Show, b: Show) {
   if (a.inProduction === b.inProduction) {
      return 0;
   } else if (a.inProduction) {
      return -1;
   } else {
      return 1;
   }
}
export function airDateComparator(a: Show, b: Show) {
   return new Date(b.airDate).valueOf() - new Date(a.airDate).valueOf();
}
export function nameComparator(a: Show, b: Show) {
   for (let i = 0; i < a.name.length+1; i++) {
      if (a.name.charAt(i) < b.name.charAt(i)) {
         return -1;
      } else if (a.name.charAt(i) > b.name.charAt(i)) {
         return 1;
      }
   }
   return 0;
}
export function addedComparator(a: Show, b: Show) {
   return 0;
}
