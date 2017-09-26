import { Component, ViewChild,AfterViewInit,HostListener} from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {StatusService } from '../_services/status.service';
import { ModalComponent } from 'ng2-bs3-modal/ng2-bs3-modal';
@Component({
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css','./profile.css']
})

export class HomeComponent implements AfterViewInit{ 
    currentUserRole;breadcrumb;_subscription;currentUserName;version;statusMsg;currentStack;
    loadingImg;getallprivateNetwork;onactive=true;settingonactive=false;projectName;userName;password;tenantid;
    checked;networkName;subnetName;chkNetwork;privateNetwork;project;projectusername;projectpassword;config;hide;networkObjArr=[];
    osOptions:any[];model: any = {};selectedVal;
    @ViewChild('openNetworksettings')
    modal: ModalComponent;

    ngAfterViewInit() {
        this.onactive=true;
        this.settingonactive=false;
        var role=  this.statusservice.getUserDetails().role;
        var project=  this.statusservice.getUserDetails().tenant.project;
        var projectusername=  this.statusservice.getUserDetails().tenant.projectusername;
        var projectpassword=  this.statusservice.getUserDetails().tenant.projectpassword;
       // activate modal if role is TA and project name,projectusername,projectpassword is empty
        // if(role==='Tenant Admin' && (project!=='' || projectusername=='' || projectpassword=='')){
        //    this.modal.open();
        // }
        this.authenticationService.getAllPrivateNetwork().subscribe(
                    (res) =>{
                      this.getallprivateNetwork=res;
                    },
                    (error)=>{
                      this.statusMsg=error.json().error;
        });
    }
   trackByIndex(index: number, obj: any): any {
    return index;
  }
  chkBoxState(tenantid){
    if(tenantid){
        return true;
    }else{
        return false;
    }
  }
 
    constructor(private statusservice:StatusService,
    			private authenticationService: AuthenticationService,
                private router:Router) {        
        this.currentUserRole = this.statusservice.getCurrentUserRole();
        this.currentUserName = this.statusservice.getUserDetails().username;
        this.tenantid=this.statusservice.getUserDetails().tenant.id;
        this.project=  this.statusservice.getUserDetails().tenant.project;
        this.projectusername=  this.statusservice.getUserDetails().tenant.projectusername;
        this.projectpassword=  this.statusservice.getUserDetails().tenant.projectpassword;
        this.breadcrumb = statusservice.breadCrumb;
        this._subscription = statusservice.breadCrumbChange.subscribe((value)=>{ 
            this.breadcrumb = value; 
        });           
        this.authenticationService.apiCallGet('misc/version').subscribe(
            (res) =>{this.version = res;},
            (error)=>{this.statusMsg=error.json().error;}
            );  
        this._subscription = statusservice.loadingImageChange.subscribe((value)=>{ 
            this.loadingImg = value; 
        });
        this.authenticationService.apiCallGet('viminfo').subscribe(
            (res) =>{this.currentStack = res.text();},
            (error)=>{this.statusMsg=error.json().error;}
            );
        if(this.currentUserRole === "Super Security Admin"){
            this.getAllVims();
            this.model.vim={};
        }else if(this.currentUserRole === "Tenant Admin"){
            this.getAllVims();
            this.model.vim={};
        } else if(this.currentUserRole === "End User"){
            this.getAllVims();
            this.model.vim={};
        } 
        
        
    }
    clickOnNext(){
          var tenantid=this.statusservice.getUserDetails().tenant.id;
          var tenantname=this.statusservice.getUserDetails().tenant.name;
          var body=JSON.stringify({
                "id" : tenantid,
                "name" : tenantname,
                "project" : this.projectName,
                "projectusername" :this.userName,
                "projectpassword" : this.password
            });
        	this.authenticationService.saveTenantProjectInfo(body).subscribe(
          (res) =>{
             this.onactive=false;
             this.settingonactive=true;
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
            
    }
    //convert integer array to array object[{id:1},{id:2}]
    toObject(arr) {
        var rv = [];
        for (var i = 0; i < arr.length; ++i)
          if (arr[i] !== undefined) 
             rv.push({"id":arr[i]});
        return rv;
    }
   /*find index of an object*/
	findIndexInData(data, property, value) {
			    var result = -1;
		    for(var i = 0, l = data.length ; i < l ; i++) {
			    if(data[i][property] === value) {
			      return i;
			    }
			  }
			   return -1;
			}
   /*delete object from array*/
	removeByAttr(arr, attr, value){
			    var i = arr.length;
			    while(i--){
			       if( arr[i] 
			           && arr[i].hasOwnProperty(attr) 
			           && (arguments.length > 2 && arr[i][attr] === value ) ){ 
			           arr.splice(i,1);

			       }
			    }
			    return arr;
			}

  
//   onChangePush(id){
//     var tenantid=this.statusservice.getUserDetails().tenant.id;
    
//        var body=JSON.stringify({        
//                 "network_name": this.networkName+id,
//                 "subnet_name": this.subnetName+id,
//                 "privateNetwork": {
//                     "id": id
//                 },
//                 "tenant": {
//                   "id": tenantid
//                 }
//          });
//   this.networkObjArr.push(body);  
//   var index=this.findIndexInData(this.networkObjArr, 'network_name',this.networkName+id);
//                 if(index== -1){
//                   this.networkObjArr.push(body);
//                }else if(index!== -1){
//                 this.removeByAttr(this.networkObjArr, 'network_name',this.networkName+id);  
//                    this.networkObjArr.push(body);
//                 }
  
//    console.log(this.networkObjArr);  
//   }
  onChecked(value,event){
     if(event.target.checked){
        this.checked = false;
        this.chkNetwork=value;
       //return value.id;
     }else if (!event.target.checked){
         this.chkNetwork=value;
         //return value.id;
     }
 }
     savePrivateNetwork(){
          var tenantid=this.statusservice.getUserDetails().tenant.id;
          var body=JSON.stringify({
                                    "network_name": this.networkName,
                                    "subnet_name": this.subnetName,
                                    "privateNetwork": {
                                        "id": this.chkNetwork
                                    },
                                    "tenant": {
                                         "id": tenantid
                                    }
                                    });
        this.authenticationService.saveTenantNetwork(body).subscribe(
          (res) =>{
             this.onactive=false;
             this.settingonactive=true;
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
            
    }
    logout(){
       this.authenticationService.deleteApiCall('login').subscribe(
            (res) =>{
                if(res){
                    this.router.navigateByUrl('/login'); 
                }else{
                	this.statusMsg="Failed to logout";
                }
            },
            (err)=>{
            	this.statusMsg=err.json().error;
            }
            );
    }
    setmodifycatalog(){
        this.statusservice.modifyCatalogStatus(false);
        this.statusservice.breadcrumb("List Catalog");
    }
    setvnflist(){
        this.statusservice.vnfListStatus(false);
        this.statusservice.breadcrumb("List VNF");
    }
     getAllVims(){
         this.authenticationService.getVims().subscribe(
          (res) =>{
             this.osOptions=res;
             this.model.vim= this.osOptions[0];//set initial or preselect option during page load
             this.setVimObject(this.model.vim);//to get rid of undefined during page load
          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
   }  
   setVimObject(vimobj){
      if(vimobj!==undefined){
        this.statusservice.setObj(vimobj);
        this.statusservice.setId(vimobj.id);
      }
       
      
   }
}