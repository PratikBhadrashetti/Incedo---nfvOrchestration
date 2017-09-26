import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Http, Headers, Response,URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx'
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';


@Component({
    templateUrl: './login.component.html',
    styleUrls:['./login.css'],
})

export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;
    mode = 'Observable';
    loginError = "";
    loginErr=false;
    statusMsg;
    passwordMessage;
    vimid;
    osOptions;
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private statusservice:StatusService) { }

    ngOnInit() {
    this.passwordMessage=this.statusservice.getPasswordChangeMessage();
    this.statusMsg = this.statusservice.getStatus();
    this.statusservice.setStatus(false);
    this.vimid=this.statusservice.getId();
   
    }

    getRoleForUser(){
        this.authenticationService.getUserInfo().subscribe(
            (res) =>{     
                if(res){
                    this.statusservice.setCurrentUserRole(res.role);                
                    this.statusservice.setUserDetails(res);
                    if(res.role === "End User"){
                         this.getAllEnduserVims();
                    }else if(res.role === "Super Security Admin"){
                      
                    //    if(this.vimid=='' || this.vimid==undefined){
                    //        this.router.navigate(['home/managevims']);
                    //    }else{
                    //        this.router.navigate(['home/dashboard']);
                    //    }
                    this.getAllVims();
                       
                }else{
                    this.getAllTenatVims();
                   // this.router.navigate(['home']);
                   
                        
                    }
                }else{
                    this.loginErr=true;
                    this.loginError = "Logon failure.";
                    this.statusservice.setStatus(false);
                }
            },
            (error)=>{
                    this.loginErr=true;
                    this.loginError = "Logon failure.";
                    this.statusservice.setStatus(false);
                    
            });
    }    
    login() {
        this.authenticationService.login(this.model.username, this.model.password).subscribe(
            (res) =>{
                if(res){
                        this.getRoleForUser();
                        this.statusservice.setPasswordChangeMessage('');
                }else{
                    this.loginErr=true;
                    this.loginError = "Logon failure.";
                    this.statusservice.setStatus(false);
                }
            },
            (error)=>{
                    this.loginErr=true;
                    this.loginError = "Logon failure.";
                    this.statusservice.setStatus(false);
                    });
    }
    keyPressFn(){
        if(this.loginErr){
            this.loginErr = false;
        }
    }
     hideStatusMsg(){
                 this.statusMsg=false;
             }
    getAllVims(){
            this.authenticationService.getVims().subscribe(
            (res) =>{
                if(res.length==0){
                    this.router.navigate(['home/managevims']);
                }else{
                    this.router.navigate(['home/dashboard']);
                    this.osOptions=res;
                    this.model.vim= this.osOptions[0];//set initial or preselect option during page load
                    this.setVimObject(this.model.vim);//to get rid of undefined during page load
                }
                
            },
            (error)=>{
                this.statusMsg=error.json().error;
            });
    }
    getAllTenatVims(){
         this.authenticationService.getVims().subscribe(
          (res) =>{
              if(res.length==0){
                  this.router.navigate(['home']);
              }else{
                  this.router.navigate(['home']);
                  this.osOptions=res;
                  this.model.vim= this.osOptions[0];//set initial or preselect option during page load
                  this.setVimObject(this.model.vim);//to get rid of undefined during page load
              }
            
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
   }   
    getAllEnduserVims(){
         this.authenticationService.getVims().subscribe(
          (res) =>{
              if(res.length==0){
                  this.router.navigate(['home/vnf']);
              }else{
                 this.router.navigate(['home/vnf']);
                  this.osOptions=res;
                  this.model.vim= this.osOptions[0];//set initial or preselect option during page load
                  this.setVimObject(this.model.vim);//to get rid of undefined during page load
              }
            
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
   }   
 setVimObject(vimobj){
      
       this.statusservice.setObj(vimobj);
       this.statusservice.setId(vimobj.id);
      
   }
}
