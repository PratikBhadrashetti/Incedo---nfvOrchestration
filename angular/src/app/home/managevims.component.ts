import { Component, ViewChild,OnInit,HostListener} from '@angular/core';
import { AuthenticationService } from '../_services/index';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {StatusService } from '../_services/status.service';
import { ModalComponent } from 'ng2-bs3-modal/ng2-bs3-modal';
@Component({
    templateUrl: './managevims.component.html'
   
})

export class ManagevimsComponent implements OnInit{ 
  statusMsg;getAllVimsData;vimname;ipaddress;adminprojectid;elkipaddress;elkid;
    @ViewChild('deleteVIM')
    modal: ModalComponent;

    ngOnInit() {
        this.statusservice.breadcrumb("Manage VIM"); 
        this.getAllVims();
     }
  
 
    constructor(private statusservice:StatusService,
    			private authenticationService: AuthenticationService,
                private router:Router) {        
    }
  
   getAllVims(){
         this.authenticationService.getVims().subscribe(
          (res) =>{
             this.getAllVimsData=res;

          },
          (error)=>{
             this.statusMsg=error.json().error;
          });
   }
  deleteVims(id){
         this.authenticationService.deleteVim(id).subscribe(
          (res) =>{
              
                 this.getAllVims();
            
        },
          (error)=>{
             this.statusMsg=error.json().error;
          }); 
       }

  openVimModal(id){
     this.modal.open();
     this.statusservice.setValue(id);
}
deleteMe(id){
   var id=this.statusservice.getValue();
   if(id!==''){
     this.deleteVims(id);
     this.modal.close();
   }
 
 
}
saveVims(){
   var body=JSON.stringify({
                             "name": this.vimname,
                             "ipaddress": this.ipaddress,
                             "adminprojectid": this.adminprojectid,
                             "elkipaddress": this.elkipaddress,
                             "elkid": this.elkid
                            });
    this.authenticationService.postVim(body).subscribe(
          (res) =>{
               this.getAllVims();
            },
          (error)=>{
             this.statusMsg=error.json().error;
          }); 
      body="";
}
  hideStatusMsg(){
            this.statusMsg=false;
    }
}

