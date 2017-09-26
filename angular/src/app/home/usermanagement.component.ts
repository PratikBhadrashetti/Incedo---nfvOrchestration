import { Component,OnInit } from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/Rx'
import {StatusService } from '../_services/status.service';
import { FormControl } from '@angular/forms';
@Component({
  templateUrl: './usermanagement.component.html',
  styleUrls: ['./usermanagement.component.css']
})
export class UsermanagementComponent implements OnInit {
    model: any = {};
    loading = false;
    passworderror=false;
    statusMsg;loadingImg;getalluser;rolesid;roleName;_subscription;
   
  constructor(private statusservice:StatusService,
        private router: Router,
        private authenticationService: AuthenticationService) { 
         this._subscription = statusservice.loadingImageChange.subscribe((value)=>{ 
            this.loadingImg = value; 
        });
    }
   // email validation in ng-input
  private validateEmail(control: FormControl) {
     var validEmailPattern = /^[a-z0-9!#$%&'*+\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$/i;
        if (!validEmailPattern.test(control.value)) {
            return {
                'erroinEmail': true
            };
        }

        return null;
    }
  public validators = [this.validateEmail];

  public errorMessages = {
        'erroinEmail': 'Email is not valid'
      };
  
  ngOnInit() {
     this.rolesid=[{
            "id":"Tenant Admin",
            "name":"Tenant Admin"
        },
        {
            "id":"End User",
            "name":"End User"
        }];
  	this.statusservice.breadcrumb("User Management");
    	this.authenticationService.getAllUser().subscribe(
          (res) =>{
             this.getalluser=res;
          },
          (error)=>{
                this.statusMsg=error.json().error;
          });
  }
  
  onChange(newValue) {
           this.roleName = newValue;
   }
  
   addEndUser() {
        var result = this.model.temail.map(function(a) {return a.value;});
          var body=JSON.stringify({
              "emailIds" : result,
              "roleName" :  this.roleName
          });
        	this.authenticationService.addUser(body).subscribe(
          (res) =>{
           
           // this.statusMsg="Please check your inbox.";
            
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
            
    }
    hideStatusMsg(){
        	this.statusMsg=false;
        }

}