import { Component } from '@angular/core';
import {StatusService } from '../_services/status.service';
import {AuthenticationService } from '../_services/index'; 
@Component({
  templateUrl:'./drmstatitics.component.html',
  styleUrls:['./Admin_dashboard/dashboard.css']
})
export class DrmstatiticsComponent {
  viewDetails;osOptions;statusMsg;osDetails ={};tenProjInfoOptions;hypervisorList=[];
  flavDet;osid;model: any = {};currentUserRole;drmDetails={};
   constructor(private statusservice:StatusService,
              private authenticationService: AuthenticationService) {
    this.currentUserRole = this.statusservice.getCurrentUserRole();
     }
     
  ngOnInit(){
        this.statusservice.breadcrumb("DRM Resources");
        this.authenticationService.drmstatitics().
          subscribe(
            (res) =>{
               this.drmDetails = res;
            },
            (error)=>{
                      if(error.status == 500){
                        this.drmDetails = {};
                      }else{
                        this.statusMsg=error.json().error;
                      }
                      }
            );  
        
      }
 }