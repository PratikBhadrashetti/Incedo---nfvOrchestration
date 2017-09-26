import {Component,OnInit} from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Router ,ActivatedRoute, Params } from '@angular/router';


@Component({	
	selector:'status',
	templateUrl:'./status.component.html',
	styleUrls:['./vnf.css']
})

export class StatusComponent implements OnInit{
	currentUserRole;vnfInstList=[];vnfInstMsg;err;statusMsg;model: any = {};
    checked = true;vnfCatObj =[];vimid;
	constructor(private authenticationService: AuthenticationService,
                private statusservice:StatusService,
                private router: Router) {
        this.currentUserRole = this.statusservice.getCurrentUserRole();
    }
    ngOnInit(){
        this.model.items='10';
        this.statusservice.breadcrumb("Status");
        this.vnfInstApiCall();
    }
    vnfInstApiCall(){
         this.vimid=this.statusservice.getId();
         this.authenticationService.getVnfInstance(this.vimid).subscribe(
            (res) =>{
                        if(res){
                            this.vnfInstList = res;
                        }else{
                            this.vnfInstMsg = "Failed to load launched instances"; 
                            this.statusservice.setStatus(this.vnfInstMsg);
                        }
                    },
            (error)=>{ this.statusMsg=error.json().error;
                    }
         );
    }
    refreshPage(){
        this.vnfInstApiCall();
    }
     hideStatusMsg(){
        	this.statusMsg=false;
        }
}