import {Component,OnInit} from '@angular/core';
import {CommonModule} from "@angular/common";
import {AuthenticationService } from '../_services/index';
import { Router } from '@angular/router';
import {StatusService } from '../_services/status.service';

@Component({
 selector:'vnf-list',
 templateUrl:'./vnflist.component.html'
 //styleUrls:['./catalogue.css']
})

export class VnfListComponent implements OnInit{
    vnfList;vnfListMsg;vnfName;catalogOptions=[];
    err = false;
    checked = true;
    vnfCatObj=[];
    showModal=false;
    statusMsg;
    launchVnfDet = false;
        sub;
    model: any = {};
    vimid;vimobj;
    ngOnInit(){
        this.statusservice.breadcrumb("List VNF");
        this.statusMsg=this.statusservice.getStatus();
        this.statusservice.setStatus(false);
        this.model.items='10';  
        this.vimid=this.statusservice.getId();  
        this.authenticationService.getVnfCatalog(this.vimid).subscribe(
            (res) =>{
                            this.vnfList = res;
                    },
            (error)=>{  this.statusMsg=error.json().error;
                    }
         );
    }
    launchVnf(vnf){
        this.catalogOptions=[];
        this.statusservice.setVnfCatalogDetails(vnf);
        this.statusservice.breadcrumb("Launch VNF");
        this.vnfName= vnf.name;
        this.launchVnfDet = true;
        this.statusservice.vnfListStatus(true);
        this.authenticationService.apiCallGet('vnf/'+vnf.id+'/catalog').subscribe(
            (res) =>{for(let cat of res){
                            this.catalogOptions.push({"id":cat.id,"name":cat.name});
                        }
                    },
            (error)=>{      this.statusMsg=error.json().error;
                    }
         );
        this.model.cat = '-1';
    }
    launchVnfSubmit(){
        this.vimobj=this.statusservice.getObj();
        var body= JSON.stringify({
                                    "description":this.model.vnfDesc,
                                    "catalog":{"id":this.model.cat},
                                    "vnf":{"id":this.statusservice.getVnfCatalogDetails().id},
                                    "tenant": this.statusservice.getUserDetails().tenant,
                                    'vim':this.vimobj
                                 });   
        this.authenticationService.launchVnfInstPost(body).subscribe(
            (res) =>{
                if(res){
                       //this.router.navigateByUrl('/home/vnf');
                       this.launchVnfDet = false;
                       this.router.navigate(['/home/vnf']);
                }else{
                       this.vnfListMsg = "VNF launch failure"; 
                       this.statusservice.setStatus(this.vnfListMsg);
                }
            },
            (error)=>{    
                this.statusMsg=error.json().error;
               }
            );
                       
        
    }
    constructor(private authenticationService: AuthenticationService,
                private router: Router,
                private statusservice:StatusService) {
        	this.sub = this.statusservice.vnfListChange.subscribe((value)=>{ 
            	this.launchVnfDet = value; 
        }); 
    }
    closeModal(){
        this.statusservice.breadcrumb("List VNF");
        this.showModal=false;
        this.launchVnfDet = false;
        this.statusMsg=false;
    }
  
   fileDownload(id) {
      
     this.authenticationService.fileDownload(id).subscribe(
            (res) =>{
                 var isChrome=(/Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor));//test chrome browser
                 var isFirefox=(navigator.userAgent.toLowerCase().indexOf('firefox') > -1);//test firefox browser
                 var isIe=(!!navigator.userAgent.match(/Trident/g) || !!navigator.userAgent.match(/MSIE/g));
                 var contentType = null;
                 contentType=res.headers.get('Content-Type');
                 var contentDispositionHeader=res.headers.get('Content-Disposition');
                 var result = contentDispositionHeader.split(';')[1].trim().split('=')[1];//get the Content-Diposition attachment; filename="x.csv"
                 var fileName=result.replace(/"/g, '');//get the file name
				 if(res && (/attachment/).test(contentDispositionHeader)){	
                       if(isChrome || isFirefox){		
                              var linkElement = document.createElement('a');
                              var blob = new Blob([res["_body"]], { type: contentType });
                              var url = window.URL.createObjectURL(blob);
                              linkElement.setAttribute('href', url);
                              linkElement.setAttribute("download", fileName);
                              var clickEvent = new MouseEvent("click", {
                                  "view": window,
                                  "bubbles": true,
                                  "cancelable": false
                              });
                              linkElement.dispatchEvent(clickEvent);
                       }else{
                            if (isIe) {
                                      var blob3;
                                          blob3 = new Blob([res["_body"]]);
                                      if (window.navigator.msSaveOrOpenBlob) {
                                            window.navigator.msSaveBlob(blob3, fileName);
                                      }
                              }
                       }
                 }
            },
            (error)=>{    this.statusMsg=error.json().error
            }
            );

  }
  
    hideStatusMsg(){
            this.statusMsg=false;
        }

    navigateTo(){
        this.router.navigateByUrl('/home/vnf');
    }
}