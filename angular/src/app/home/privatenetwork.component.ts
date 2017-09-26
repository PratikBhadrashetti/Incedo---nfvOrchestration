import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Tenant } from '../tenant/tenant.component';
import {UtilityService } from '../_services/utility';
@Component({
 
  templateUrl: './privatenetwork.component.html'
  
})
export class PrivatenetworkComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;tenants;getallprivateNetwork;networkName;subnetName;privateNetwork;getAllTenantNetwork;
    vimid;body;vimobj;
   constructor(private authenticationService: AuthenticationService,private utilityservice:UtilityService,
  			   private statusservice:StatusService) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Tenant Network");
    	 this.authenticationService.getAllPrivateNetwork().subscribe(
                    (res) =>{
                      this.getallprivateNetwork=res;
                    },
                    (error)=>{
                      this.statusMsg=error.json().error;
        });
         this.vimid=this.statusservice.getId();
        	 this.authenticationService.getAllTenantNetwork(this.vimid).subscribe(
                    (res) =>{
                      this.getAllTenantNetwork=res;
                    },
                    (error)=>{
                      this.statusMsg=error.json().error;
        });
  }
  onChange(newValue) {
  
              this.privateNetwork = newValue;
    
 }
   savePrivateNetwork(){
         this.vimobj=this.statusservice.getObj();
          var tenantid=this.statusservice.getUserDetails().tenant.id;
          this.body={
                                    "network_name": this.networkName,
                                    "subnet_name": this.subnetName,
                                    "privateNetwork": {
                                        "id": parseInt(this.privateNetwork)
                                    },
                                    "tenant": {
                                         "id": tenantid
                                    },
                                    'vim':this.vimobj
                                  };
         //this.utilityservice.objectMix(this.vimobj, this.body);
         this.authenticationService.saveTenantNetwork(this.body).subscribe(
          (res) =>{
            this.vimid=this.statusservice.getId();
            this.authenticationService.getAllTenantNetwork(this.vimid).subscribe(
                    (res) =>{
                      this.getAllTenantNetwork=res;
                    },
                    (error)=>{
                  this.statusMsg=error.json().error;
        });
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
            
    }
    hideStatusMsg(){
        	this.statusMsg=false;
        }

}