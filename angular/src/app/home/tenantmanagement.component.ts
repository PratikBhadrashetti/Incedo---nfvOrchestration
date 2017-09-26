import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Tenant } from '../tenant/tenant.component';
import {Router} from '@angular/router';
@Component({
  selector: 'app-tenantmanagement',
  templateUrl: './tenantmanagement.component.html',
  styleUrls: ['./tenantmanagement.component.css']
})
export class TenantmanagementComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;tenants;getAllTenantinfo;allocatedip;tenantvims;showtenantvim=false;
    vimoptions=[];disbtn=false;tenantvimsunassigned;getAllocatedUsedIp;
   
   constructor(private authenticationService: AuthenticationService,
  			   private statusservice:StatusService,private router:Router) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Tenant Management");
    	this. getTenantall();
      	// this.authenticationService.getAllTenant().subscribe(
        //   (res) =>{
        //      this.getAllTenantinfo=res;
        //   },
        //   (error)=>{
        //         this.statusMsg=error.json().error;
        //   });
  }
    getTenantall(){
        this.authenticationService.getAllTenant().subscribe(
            (res) =>{
              this.tenants=res;
            },
            (error)=>{
                  this.statusMsg=error.json().error;
            });
    }
   saveTenanat() {
        
          var body=JSON.stringify({
            
              "email" : this.model.tenantEmail,
              "tenantname" : this.model.tenantName

          });
        	this.authenticationService.saveTenant(body).subscribe(
          (res) =>{
            //this.statusMsg="Please check your inbox at "+this.model.tenantEmail;
             	this. getTenantall();
          },
          (error)=>{
                this.statusMsg=error.json().error;
          });
            
    }
    incallocatedIP(vimId) {
      var tenantid=this.statusservice.getValue();
      this.authenticationService.incAllocatedIP(vimId,tenantid).subscribe(
          (res) =>{
             	//this. getTenantall();
                var id=this.statusservice.getValue();
                this.getTenantVims(id);
          },
          (error)=>{
                this.statusMsg=error.json().error;
          });
  }
    decallocatedIP(vimId) {
      var tenantid=this.statusservice.getValue();
      this.authenticationService.deAllocatedIP(vimId,tenantid).subscribe(
          (res) =>{
             	//this. getTenantall();
                 var id=this.statusservice.getValue();
                this.getTenantVims(id);
          },
          (error)=>{
                this.statusMsg=error.json().error;
          });
  }
  getProp(prop) {
    return this[prop];
    
  }

  setProp(prop, value) {
    this[prop] = value;
   }
  allocateStatus(usedip,allocatedip){
      if((usedip >allocatedip) || (usedip == allocatedip)){
          return true;
      }else{
          return false;
      }
  }
  sendTenantID(id,name){
    this.statusservice.breadcrumb(name+"-Assign VIM");
    this.showtenantvim=true;
    this.getTenantVims(id);
    this.getTenantUnassignedVims(id)
    this.statusservice.setValue(id);
   }
   getTenantVims(id){
         this.authenticationService.getTenantVims(id).subscribe(
            (res) =>{
              this.tenantvims=res;
              var id=this.statusservice.getValue();
                for(var fl of res){
                  this.authenticationService.getTenantVimsAllocatedIp(fl.id,id).subscribe( (res) =>{
                      this.getAllocatedUsedIp=res;
                    },
                    (error)=>{
                          this.statusMsg=error.json().error;
                        
                    });
                }
            },
            (error)=>{
                  this.statusMsg=error.json().error;
            });
    }
  getTenantUnassignedVims(id){
         this.authenticationService.getTenantUnassignedVims(id).subscribe(
            (res) =>{
              this.tenantvimsunassigned=res;
               
            },
            (error)=>{
                  this.statusMsg=error.json().error;
            });
    }
    cancelTenantVim(){
        this.showtenantvim=false;
        this.statusservice.breadcrumb("Tenant Management");
    }
    getBodyFromListVim(body){
    
      var id= this.statusservice.getValue();
      this.assignVim(id,body);
      this.statusservice.breadcrumb("Tenant Management");
      this.showtenantvim=false;
     
     }
    assignVim(id,body){
       this.authenticationService.assignTenantVim(id,body).subscribe(
            (res) =>{
               var id= this.statusservice.getValue();
              this.getTenantVims(id);
              this.getTenantUnassignedVims(id);
              
            },
            (error)=>{
              this.statusMsg=error.json().error;
      });
    }
  getUsedIpAllocatedIp(){

  }
  hideStatusMsg(){
        	this.statusMsg=false;
  }
 
}