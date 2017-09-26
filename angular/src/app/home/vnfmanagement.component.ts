import {Component} from '@angular/core';
import { Router } from '@angular/router';


@Component({
	
	selector:'vnf-management',
	templateUrl:'./vnfmanagement.component.html',
	 styleUrls:['./vnf.css']
})

export class vnfManagementComponent{
	currentUser;
    constructor(private router:Router){
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }
    action(){
    	this.router.navigateByUrl('/home/adduser');
    }

}