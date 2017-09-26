import {Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/Rx'
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import * as GlobalVariable from "../_services/constants";

@Component({	
	selector:'catalogue',
	templateUrl:'./catalogue.component.html',
	styleUrls:['./catalogue.css']
})

export class CatalogueComponent implements OnInit{
	catList=[];
	err = false;
	viewCatMsg = '';
	statusMsg;
	show;
	constructor(private authenticationService: AuthenticationService,private router: Router,
		private statusservice:StatusService) {}

    onSelect(cat) {
     	this.statusservice.setCatalogDetails(cat.name,cat.id);
    	this.router.navigateByUrl('/home/detail');
  	}
	ngOnInit() {
		this.statusservice.breadcrumb("List Catalog");
        this.statusMsg=this.statusservice.getStatus();
        this.statusservice.setStatus(false);
		var vimid=this.statusservice.getId();
		this.authenticationService.getCatalog(vimid).subscribe(
			(res) =>{	  
	                    this.catList = res;
	                    localStorage.setItem('catalogueList',JSON.stringify(this.catList));
			                for(var cat of this.catList){
			                	cat.image = GlobalVariable.BASE_API_URL+'catalog/'+cat.id+'/logo';
			                	}
                },
         	(error)=>{
         			this.statusMsg=error.json().error;
         		 		       			}
         	);
	}
	hideStatusMsg(){
        	this.statusMsg=false;
        }
	
}