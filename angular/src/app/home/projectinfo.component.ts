import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Tenant } from '../tenant/tenant.component';
@Component({
 
  templateUrl: './projectinfo.component.html'
  
})
export class ProjectinfoComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;tenants;projectName;userName;password;
    
   constructor(private authenticationService: AuthenticationService,
  			   private statusservice:StatusService) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Project Info");
    	
  }
    clickOnSave(){
          var tenantid=this.statusservice.getUserDetails().tenant.id;
          var tenantname=this.statusservice.getUserDetails().tenant.name;
          var body=JSON.stringify({
                "id" : tenantid,
                "name" : tenantname,
                "project" : this.model.projectName,
                "projectusername" :this.model.userName,
                "projectpassword" : this.model.password
            });
        	this.authenticationService.saveTenantProjectInfo(body).subscribe(
          (res) =>{
            this.statusMsg="Successfully saved.";
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
          this.model={};      
    }
    hideStatusMsg(){
        	this.statusMsg=false;
        }

}