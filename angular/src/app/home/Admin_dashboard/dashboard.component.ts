import { Component } from '@angular/core';
import {StatusService } from '../../_services/status.service';
import {AuthenticationService } from '../../_services/index'; 
@Component({
   selector: 'app-root',
  templateUrl:'./dashboardfrontpage.component.html',
  styleUrls:['./dashboard.css']
})
export class DashComponent {
  viewDetails;osOptions;statusMsg;osDetails ={};tenProjInfoOptions;hypervisorList=[];drmDetails;
  flavDet;osid;model: any = {};currentUserRole;hypervisor ={};tAdminProjId;tAdminProjName;optionId;showTbl;vimid;
   constructor(private statusservice:StatusService,
              private authenticationService: AuthenticationService) {
    this.currentUserRole = this.statusservice.getCurrentUserRole();
     }
     instDetails;filterargs;
  ngOnInit(){
         var res = { "servers": [
                  {"id": "8639f9ef5a2d46af9a1d9cfd0db9564a",
                  "links":{
                        "href": "http://10.145.251.11/horizon/admin/instances/",
                        "ip": "10.145.251.11",
                        "name": "admin" }}]};

        this.model.items='10';    
        this.model.searchItem='';
        this.statusservice.breadcrumb("Dashboard");
        this.osOptions = res.servers;
        this.model.vim = '8639f9ef5a2d46af9a1d9cfd0db9564a';
        if(this.currentUserRole == 'Super Security Admin'){
          this.model.tenProjInfo='-1';
          this.authenticationService.apiCallGet('getalltenants').subscribe(
            (res) =>{   
                this.tenProjInfoOptions = res;
                    },
            (error)=>{               
                  this.statusMsg=error.json().error;
                }
            );  
          this.vimid=this.statusservice.getId();
          this.viewDet('-1');
           this.authenticationService.drmstatitics().
          subscribe(
            (res) =>{
               this.drmDetails = res;
            },
            (error)=>{
                      if(error.status == 500){
                        this.drmDetails = {};
                      }else{
                        this.statusMsg=error.json().error;
                      }
                      }
            );  
           
        }else if(this.currentUserRole =='Tenant Admin'){
          if(this.statusservice.getUserDetails().tenant!=null){
            this.tAdminProjId=this.model.tenProjInfo= this.statusservice.getUserDetails().tenant.projectid;
            this.tAdminProjName =this.statusservice.getUserDetails().tenant.name;
            this.viewDet(this.statusservice.getUserDetails().tenant.projectid);
          }
        }
               
      }
 
  viewDet(os){
    //var id = os;
    if(os==='-1'){
      this.vimid=this.statusservice.getId();
      this.authenticationService.oshypervisorsDetailGet(this.vimid).subscribe(
            (res) =>{   
                //this.hypervisorList = res.hypervisor;
                   this.hypervisor = res.hypervisors[0];
                   this.flavMapping1(os);
                
               
                // id = '8639f9ef5a2d46af9a1d9cfd0db9564a';
                
             },
            (error)=>{               
                  this.statusMsg=error.json().error;
                
                }
            );    
    }else{
    this.vimid=this.statusservice.getId();
    this.authenticationService.openstackInstanceLimitGet(this.vimid,os).subscribe(
            (res) =>{   
               this.osDetails = res.limits.absolute;
               this.flavMapping2(os);
            },
            (error)=>{
               this.statusMsg=error.json().error;
               
              }
                      
            );
    }
  }
  flavMapping1(id){
    this.vimid=this.statusservice.getId();
      this.authenticationService.openstackInstanceServerDetails(this.vimid,id).
          subscribe(
            (res) =>{
               this.instDetails = res.servers;
               this.flavDet = {};
               for(var fl of res.servers){
                    if(fl.flavor.id == '4'){
                    fl.flavDet ={'vcpu':'m1.large, 4',
                                  'ram':'8 GB',
                                  'rootDisk':'80 GB'};
                   }else if(fl.flavor.id == '3'){
                     fl.flavDet ={'vcpu':'m1.medium, 2',
                                  'ram':'4 GB',
                                  'rootDisk':'40 GB'};
                   }else if(fl.flavor.id == '2'){
                     fl.flavDet ={'vcpu':'m1.small, 1',
                                  'ram':'2 GB',
                                  'rootDisk':'20 GB'};
                    }else if(fl.flavor.id == '1'){  
                       fl.flavDet ={'vcpu':'m1.tiny,1',
                                  'ram':'512 MB',
                                  'rootDisk':'1 GB'};
                    }else if(fl.flavor.id == '5'){
                       fl.flavDet ={'vcpu':'xlarge,8',
                                  'ram':'16 GB',
                                  'rootDisk':'160 GB'};
                    }
                 }
            
              
              },
            (error)=>{
                      if(error.status === 500){
                        this.instDetails = [];
                      }else{
                        this.statusMsg=error.json().error;
                      }
                      }
            );  
  
  }

    flavMapping2(id){
       this.vimid=this.statusservice.getId();
       this.authenticationService.openstackInstanceServerDetails(this.vimid,id).
          subscribe(
            (res) =>{
               this.instDetails = res;
               this.flavDet = {};
               for(var fl of res){
                    if(fl.flavorid == '4'){
                    fl.flavDet ={'vcpu':'m1.large, 4',
                                  'ram':'8 GB',
                                  'rootDisk':'80 GB'};
                   }else if(fl.flavorid == '3'){
                     fl.flavDet ={'vcpu':'m1.medium, 2',
                                  'ram':'4 GB',
                                  'rootDisk':'40 GB'};
                   }else if(fl.flavorid == '2'){
                     fl.flavDet ={'vcpu':'m1.small, 1',
                                  'ram':'2 GB',
                                  'rootDisk':'20 GB'};
                    }else if(fl.flavorid == '1'){  
                       fl.flavDet ={'vcpu':'m1.tiny,1',
                                  'ram':'512 MB',
                                  'rootDisk':'1 GB'};
                    }else if(fl.flavorid == '5'){
                       fl.flavDet ={'vcpu':'xlarge,8',
                                  'ram':'16 GB',
                                  'rootDisk':'160 GB'};
                    }
                 }
            
              
              },
            (error)=>{
                      if(error.status === 500){
                        this.instDetails = [];
                      }else{
                        this.statusMsg=error.json().error;
                      }
                      }
            );  
  
  }
}