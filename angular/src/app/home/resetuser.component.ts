import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-resetuser',
  templateUrl: './resetuser.component.html'
  // styleUrls: ['./resetuser.component.css']
})
export class ResetuserComponent implements OnInit {

private userOptions =[];loading = false;passworderror=false;
statusMsg;model: any = {};userId: any = {};
  constructor(private authenticationService: AuthenticationService,
  			  private statusservice:StatusService,private router: Router) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Reset User");
  	this.authenticationService.apiCallGet('user/all').subscribe(
            (res) =>{     
                        for(var user of res){
                            this.userOptions.push({ id: user.id, name: user.name});
                        }
                },
            (error)=>{
                        this.statusMsg=error.json().error;
                      }
            );
  }
  resetUser(){
    this.userId.id=this.model.usersList;
  	this.loading = true;
    if(this.model.password!==this.model.confirmpassword){
        	this.passworderror=true;
    }else{
          var body = JSON.stringify({
             'newPassword':this.model.password
          });
          this.authenticationService.resetUser(body,this.model.usersList).subscribe(
            (res)=>{
              if(res.status==200){
                this.router.navigateByUrl('catalogue');
              }
            },
            (err)=>{
                  //Handle logout failure}
                  this.statusMsg=err.json().error;
                }
            )} 
  }
  hideStatusMsg(){
        	this.statusMsg=false;
  }
}
