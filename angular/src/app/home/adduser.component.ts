import {Component} from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { Router } from '@angular/router';

@Component({	
	selector:'add-user',
	templateUrl:'./adduser.component.html',
	styleUrls:['./catalogue.css','./adduser.css']
})

export class addUserComponent{
	users;vnfUsrObj=[];vnfId;statusMsg;addUsrMsg;catId;
	constructor(private authenticationService: AuthenticationService,
                private statusservice:StatusService,
                private router:Router){
        this.vnfId = this.statusservice.getLaunchedVnfDet().id;
        this.catId = this.statusservice.getLaunchedVnfDet().catalog.id;
    }
	ngOnInit() {
        this.statusservice.breadcrumb("Manage VNF Instances");
		this.loadAllUsers();
        
	}
	private loadAllUsers() {
         this.authenticationService.apiCallGet('catalog/'+this.catId+'/user').subscribe(
            (res) =>{   
                    if(res){  
                        this.users = res;
                        this.loadSelectedUsers();
                    }else{
                            this.addUsrMsg = "Failed to load users"; 
                            this.statusservice.setStatus(this.addUsrMsg);
                    }
                },
            (error)=>{
                this.statusMsg=error.json().error;
            }
            );
    }
    private loadSelectedUsers(){ 
        this.authenticationService.apiCallGet('vnfinstance/'+this.vnfId+'/user').subscribe(
            (res) =>{
                if(res){
                    var user = this.users;
                    for(let usr of res){
                        for(let allusr of user){
                            if(allusr.id === usr.id){
                                allusr.checked =true;
                                this.vnfUsrObj.push({"id":usr.id});
                            }
                        }
                    }
                    this.users = user;
                }else{
                            this.addUsrMsg = "Failed to load selected users"; 
                            this.statusservice.setStatus(this.addUsrMsg);
                    }
            },
            (error)=>{
                this.statusMsg=error.json().error;
            }
        );
    }
    userChecked(value,event){
         if(event.target.checked){
            this.vnfUsrObj.push({"id":value.id});
         }else if (!event.target.checked){
            this.vnfUsrObj = this.vnfUsrObj.filter(function(el){ return el.id != value.id; });
         }
    }
    vnfUserMap(){
        this.authenticationService.userToVnfPatch(this.vnfId,this.vnfUsrObj).subscribe(
            (res) =>{
                this.router.navigateByUrl('/home/vnf');
            },
            (error)=>{
                this.statusMsg=error.json().error;
            }
        );
    }

    hideStatusMsg(){
            this.statusMsg=false;
        }
    goBack(){
       this.router.navigateByUrl('/home/vnf');
    }
}