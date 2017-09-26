import {Component,ViewChild,AfterViewInit,HostListener} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import { ModalComponent } from 'ng2-bs3-modal/ng2-bs3-modal';
import {UtilityService } from '../_services/utility';
@Component({
  selector:'catalogue-detail',
  templateUrl:'./cataloguedetail.component.html',
  styleUrls:['./cataloguedetails.css']
})

export class CatalogueDetailComponent implements AfterViewInit{
 currentUserRole;
 catalogId;vnf;
 catalogName;
 vnfList;
 catalog;
 catList=[];
 err = false;
 catDetMsg = '';
 arr= [];
 vnfCatObj =[];
 vnfAssociated=[];
 statusMsg;
 checked = true;
 disableconfirmbtn=false;
 cat;
 model: any = {};
 tenantid;
 vimobj;
  @ViewChild('openCatalogConfirm')
    modal: ModalComponent;
 ngAfterViewInit() {
 }

 constructor(private route: ActivatedRoute,
             private router: Router,
             private authenticationService: AuthenticationService,
             private statusservice:StatusService,private utilityservice:UtilityService) {
         this.currentUserRole = this.statusservice.getCurrentUserRole();
         this.tenantid=this.statusservice.getUserDetails().tenant.id;
     }

  ngOnInit() {
    this.model.items='10';
    this.statusMsg=this.statusservice.getStatus();
    this.statusservice.setStatus(false);
    this.cat=this.statusservice.getCatalogDetails();
    this.catalogName=this.cat.name;
    this.catalogId=this.cat.id;
    this.statusservice.breadcrumb("Catalog Detail - " +this.catalogName);
    this.getAssocitedVnfFn();
  }
 getAssocitedVnfFn(){
   this.authenticationService.apiCallGet('catalog/'+this.catalogId+'/vnf').subscribe(
       (res) =>{
        
          this.vnfList = res;
          this.statusservice.setVnf(this.vnfList);
          for(var vnf of res){
              this.vnfAssociated.push({"id":vnf.id});
          }
        },
        (error)=>{this.statusMsg=error.json().error;
                 }
    );
 } 
 vnfChecked(value,event){
     if(event.target.checked){
        this.checked = false;
        this.vnfCatObj.push({"id":value.id});
     }else if (!event.target.checked){
        this.vnfCatObj = this.vnfCatObj.filter(function(el){ return el.id != value.id; });
        if(this.vnfCatObj.length==0){
          this.checked = true;
         }
     }
 }

 dissVnfFn(){
  var assVNF = this.vnfAssociated;
  for(let vnf of this.vnfCatObj){
    assVNF = assVNF.filter(function(el){ return el.id != vnf.id; })
  }
  this.authenticationService.dissociateVnfCall(this.catalogId,assVNF).subscribe(
   (res) =>{
               if(res){
                  this.getAssocitedVnfFn();                        
                }else{}
            },
          (error)=>{this.statusMsg=error.json().error;
                   }
         );
  }
 
  registerVnf(vnf){
      this.authenticationService.vnfRegister(vnf).subscribe(
   (res) =>{
          if(res){
               this.statusMsg="";               
           }else{

                }
            },
          (error)=>{
              this.statusMsg=error.json().error;
                   }
         );
  }
  
 lunchModal(vnf) {
        if(vnf.tenant!==null && this.tenantid!==vnf.tenant.id){
           this.modal.open();
           this.statusservice.setValue(vnf);
        }else{
           this.disableconfirmbtn=true;
            
        }
    }
  
  registerVnfModal(){
     this.registerVnf(this.statusservice.getValue());
     this.modal.close();
  }  
}