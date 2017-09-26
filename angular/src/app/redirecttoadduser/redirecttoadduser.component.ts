import { Component,OnInit } from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Observable } from 'rxjs/Observable';
import { Router ,ActivatedRoute} from '@angular/router';
import 'rxjs/Rx'
import {StatusService } from '../_services/status.service';

@Component({    
    templateUrl: './redirecttoadduser.component.html'
    
})

export class RedirecttoadduserComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;_subscription;tenants;tenantId;
    private sub: any;     
    private token: string; 
    private pemail: string; 
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
          this.pemail = params['ptemail']; 
          this.tenantname = params['tenantname']; 
          this.model.temail= this.pemail;
          console.log(this.pemail);
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
    onChange(newValue) {
                    this.tenantId = newValue;
     }
    
    registerTenant() {
        if(this.model.password!==this.model.confirmpassword){
        	this.passworderror=true;
        } else {
       
	        var body = JSON.stringify({
                'firstname':this.model.firstname,
                'lastname':this.model.lastname,
                'username':this.model.username,
                'email':this.model.temail,
                'tenantname': this.model.tenant,
                'token': this.token
              }); 
        	this.authenticationService.registerTenant(body).subscribe(
			(res) =>{
               // this.statusMsg="Please check your inbox at "+this.model.temail+" to open the confirmation mail.";
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
