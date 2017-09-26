import {Component,OnInit} from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Router ,ActivatedRoute, Params } from '@angular/router';


@Component({	
	selector:'vnf',
	templateUrl:'./vnf.component.html'
	//styleUrls:['./vnf.css']
})

export class VnfComponent implements OnInit{
	currentUserRole;vnfInstList=[];vnfInstMsg;err;statusMsg;model: any = {};vnfId;
    checked = true;vnfCatObj =[];showModal= false;vimid;
	constructor(private authenticationService: AuthenticationService,
                private statusservice:StatusService,
                private router: Router) {
        this.currentUserRole = this.statusservice.getCurrentUserRole();
    }
    ngOnInit(){
        this.model.items='10';
        this.statusservice.breadcrumb("List VNF Instances");
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
    action(vnf){
        this.statusservice.setLaunchedVnfDet(vnf);
        this.router.navigateByUrl('/home/adduser');
    }

    refreshPage(){
        this.vnfInstApiCall();
    }
    viewVnf(vnf){
        this.authenticationService.apiCallGet('/viewvuic/'+vnf.id).subscribe(
            (res) =>{
                    window.open(res.text(),'_blank');
                    },
            (error)=>{ 
                        this.statusMsg=error.json().error;
                    }
         );
    }
     assignExternalIp(vnf){
        this.authenticationService.assignExternalIp(vnf.id).subscribe(
            (res) =>{
                    // this.statusMsg="External IP assigned.";
                     this.vnfInstApiCall();
                    },
            (error)=>{ 
                    this.statusMsg=error.json().error;
                    }
         );
    }
    deleteConf(id){
        this.vnfId= id;
        this.showModal=true;
    }
    deleteVnfFn(){
      
      this.authenticationService.deleteApiCall('/vnfinstance/'+this.vnfId).subscribe(
        (res) =>{
                    if(res){
                            this.vnfInstApiCall();
                    }
                },
        (err)=>{
                  this.statusMsg=err.json().error;
               }
      );
      this.vnfId = null;
    }
     closeModal(){
        this.showModal=false;
    }
hideStatusMsg(){
            this.statusMsg=false;
        }
}