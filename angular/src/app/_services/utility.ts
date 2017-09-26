import { Injectable } from '@angular/core';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class UtilityService {
    constructor() {
                }

objectMix(source, target) {
  //  for(var key in source) {
  //    if (source.hasOwnProperty(key)) {
  //       target[key] = source[key];
  //    }
  //  }
	 for(var key in source) {
    target[key] = source[key];
}

}
 findIndexInData(data, property, value) {
			    var result = -1;
			  for(var i = 0, l = data.length ; i < l ; i++) {
			    if(data[i][property] === value) {
			      return i;
			      //return true;
			    }
			  }
			  return -1;
			}
  removeByAttr(arr, attr, value){
			    var i = arr.length;
			    while(i--){
			       if( arr[i] 
			           && arr[i].hasOwnProperty(attr) 
			           && (arguments.length > 2 && arr[i][attr] === value ) ){ 

			           arr.splice(i,1);

			       }
			    }
			    return arr;
			}

}