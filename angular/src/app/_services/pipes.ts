import { Pipe, PipeTransform } from '@angular/core';
import { Injectable } from '@angular/core';
@Pipe({
    name: 'myfilter',
    pure: false
})
@Injectable()
export class MyFilterPipe implements PipeTransform {
    transform(items: any[], args: any[]): any {
    	 if (items==null) {
		      return null;
		    }
        // filter items array, items which match and return true will be kept, false will be filtered out
        /*return items.filter(item => item.title.indexOf(args[0].title) !== -1);*/
        items = items.filter(function(el){ return el.name.indexOf(args[0].name) !== -1 });
        return items;
    }
}