import { Component, OnInit } from '@angular/core';
import {StatusService } from '../_services/status.service';
import {AuthenticationService } from '../_services/index';
import { Router } from '@angular/router';

@Component({
  selector: 'app-changepassword',
  templateUrl: './changepassword.component.html'
//   styleUrls: ['./changepassword.component.css']
})
export class ChangepasswordComponent implements OnInit {
	model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;
  constructor(private statusservice:StatusService,private router: Router,
             private authenticationService: AuthenticationService) { }

  ngOnInit() {

  	this.statusservice.breadcrumb("Change Password" );
  }

  changePassword() {
        this.loading = true;
        if(this.model.password!==this.model.confirmpassword){
        	this.passworderror=true;
        } else {
	        var body = JSON.stringify({
	        	'oldPassword':this.model.oldpassword,
                'newPassword':this.model.password
                
            }); 
	        	this.authenticationService.changePassword(body).subscribe(
				(res) =>{
					if(res.status==200){
					this.authenticationService.deleteApiCall('login').subscribe(
		            (res) =>{
		                if(res){
		                        this.statusservice.setPasswordChangeMessage('Password updated. Please login again');
		                        this.router.navigateByUrl('login');
		                }else{
		                	//Handle logout error
		                }
		            },
		            (err)=>{
		            	this.statusMsg=err.json().error;
		            }
		            );					
				}
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
