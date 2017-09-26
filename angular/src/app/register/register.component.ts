import { Component,OnInit } from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/Rx'
import {StatusService } from '../_services/status.service';
import { Tenant } from '../tenant/tenant.component';
@Component({    
    templateUrl: './register.component.html',
    styleUrls:['./register.component.css']
})

export class RegisterComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    tenantId;
    statusMsg;loadingImg;_subscription;tenants;
    ngOnInit() {
  
    	this.authenticationService.getAllTenant().subscribe(
          (res) =>{
            this.tenants=res;
          },
          (error)=>{
                this.statusMsg=error.json().error;
          });
  }
    constructor(private statusservice:StatusService,
        private router: Router,
        private authenticationService: AuthenticationService) { 
         this._subscription = statusservice.loadingImageChange.subscribe((value)=>{ 
            this.loadingImg = value; 
        });
    }
    
  onChange(newValue) {
  
               this.tenantId = newValue;
    
 }
    register() {
        if(this.model.password!==this.model.confirmpassword){
        	this.passworderror=true;
        } else {
            // var tenantId;
            //  for(var i=0;i<this.model.tenant.length;i++){
            //              tenantId=this.model.tenant[i];
            //  }
            
	        var body = JSON.stringify({
                'firstname':this.model.firstname,
                'lastname':this.model.lastname,
                'username':this.model.username,
                'email':this.model.email,
                'tenantid': parseInt(this.tenantId)
                
            }); 
        	this.authenticationService.userregister(body).subscribe(
			(res) =>{
               // this.statusMsg="Your email id is not yet confirmed. Please check your inbox at "+this.model.email+"to open the confirmation mail.";
				//this.router.navigateByUrl('/login');
			},
			(error)=>{
            this.statusMsg=error.json().error;
            });
        }
    }
    hideStatusMsg(){
        	this.statusMsg=false;
        }
}
