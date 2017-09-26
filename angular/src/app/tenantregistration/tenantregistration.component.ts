import { Component,OnInit } from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Observable } from 'rxjs/Observable';
import { Router ,ActivatedRoute} from '@angular/router';
import 'rxjs/Rx'
import {StatusService } from '../_services/status.service';

@Component({    
    templateUrl: './tenantregistration.component.html',
    styleUrls:['./tenantregistration.component.css']
})

export class TenantregistrationComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;_subscription;tenants;
    private sub: any;     
    private token: string; 
    private temail: string; 
    private tenantname: string; 

    ngOnInit() {
  
    	this.authenticationService.getAllTenant().subscribe(
          (res) =>{
            this.tenants=res;
          },
          (error)=>{
            this.statusMsg=error.json().error;
          });

           // get URL parameters
    this.sub = this.route
        .params
        .subscribe(params => {
          this.token = params['token']; 
          this.temail = params['email']; 
          this.tenantname = params['tenantname']; 
          this.model.email= this.temail;
          this.model.tenant= this.tenantname;
    });
  }
    constructor(private statusservice:StatusService,private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService) { 
         this._subscription = statusservice.loadingImageChange.subscribe((value)=>{ 
            this.loadingImg = value; 
        });
    }

    registerTenant() {
        if(this.model.password!==this.model.confirmpassword){
        	this.passworderror=true;
        } else {
            var tenantId;
                for(var i=0;i<this.model.tenant.length;i++){
                         tenantId=this.model.tenant[i];
             }
	        var body = JSON.stringify({
                'firstname':this.model.firstname,
                'lastname':this.model.lastname,
                'username':this.model.username,
                'email':this.model.email,
                'tenantname': this.model.tenant,
                'token': this.token
                
            }); 
        	this.authenticationService.registerTenant(body).subscribe(
			(res) =>{
               // this.statusMsg="Please check your inbox at "+this.model.email+"to open the confirmation mail.";
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
