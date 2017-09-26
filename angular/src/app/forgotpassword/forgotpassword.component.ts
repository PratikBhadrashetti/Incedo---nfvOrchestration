import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Http, Headers, Response,URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx'
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';


@Component({
    templateUrl: './forgotpassword.component.html',
    styleUrls:['./forgotpassword.component.css'],
})

export class ForgotpasswordComponent implements OnInit {
    model: any = {};
    loading = false;
    mode = 'Observable';
    loginError = "";
    loginErr=false;
    statusMsg;
    passwordMessage;loadingImg;_subscription;
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private statusservice:StatusService) {

        this._subscription = statusservice.loadingImageChange.subscribe((value)=>{ 
            this.loadingImg = value; 
        });
         }

    ngOnInit() {
    this.statusMsg = this.statusservice.getStatus();
    this.statusservice.setStatus(false);
    }
    sendForgotpasswordEmail(){
         
                  
	        var body = JSON.stringify({
               'username':this.model.username
                            
            }); 
        	this.authenticationService.forgotPassword(body).subscribe(
			(res) =>{
                //this.statusMsg="Please check your inbox for reset password link.";
				//this.router.navigateByUrl('/login');
			},
			(error)=>{
               this.statusMsg=error.json().error;
            });
 
    }
     hideStatusMsg(){
                 this.statusMsg=false;
             }
}
