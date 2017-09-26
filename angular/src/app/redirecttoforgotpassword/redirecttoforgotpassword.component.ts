import { Component,OnInit } from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Observable } from 'rxjs/Observable';
import { Router,ActivatedRoute} from '@angular/router';
import 'rxjs/Rx'
import {StatusService } from '../_services/status.service';

@Component({    
    templateUrl: './redirecttoforgotpassword.component.html',
    styleUrls:['./redirecttoforgotpassword.component.css']
})

export class RedirecttoforgotpasswordComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;_subscription;tenants;
    private sub: any;     
    private token: string;  
  
    constructor(private statusservice:StatusService,private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService) { 
         this._subscription = statusservice.loadingImageChange.subscribe((value)=>{ 
            this.loadingImg = value; 
        });
    }
      ngOnInit() {
     // get URL parameters
    this.sub = this.route
        .params
        .subscribe(params => {
          this.token = params['token']; // --> Name must match wanted paramter
          
    });
  }

    setForgotPassword(){
        if(this.model.password!==this.model.confirmpassword){
        	this.passworderror=true;
        }else{
            var body = JSON.stringify({
                "newPassword":this.model.password
             }); 
        	this.authenticationService.setForgotPassword(body, this.token).subscribe(
			(res) =>{
               // this.statusMsg="Password saved successfully.";
				this.router.navigate(['../login']);
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
