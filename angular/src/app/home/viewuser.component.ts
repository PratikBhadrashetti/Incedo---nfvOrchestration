import {Component} from '@angular/core';
import { viewUserService } from './viewuser.service';


@Component({
	
	selector:'view-user',
	templateUrl:'./viewuser.component.html',
	styleUrls:['./viewuser.component.css']
})

export class viewUserComponent{
	users;table1;table2=true;
	options = [
        {
            value: 'a',
            label: 'Huawei'
        },
        {
            value: 'c',
            label: 'Cisco'
        },
         {
            value: 'd',
            label: 'Juniper'
        }
    ];
	constructor(private viewuserservice:viewUserService){}

	ngOnInit() {
		this.loadAllUsers();
	}

	private loadAllUsers() {
        this.viewuserservice.getUsers().subscribe(users => { this.users = users; });
    }

    newUserTable(){
    	this.table1=false;
    	this.table2=true;
    }

     allUserTable(){
     	this.table2=false;
     	this.table1='true';
     }
    

}