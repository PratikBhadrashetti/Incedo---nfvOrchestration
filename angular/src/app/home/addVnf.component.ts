import {Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx'
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import {Router} from '@angular/router';

@Component({
 
 selector:'add-vnf',
 templateUrl:'./addvnf.component.html',
 styleUrls:['./vnf.css']
})

export class addVnfComponent implements OnInit{
    resp;
    err = false;
    catCreateMsg = '';
    vnfListErr = '';
    asVnfMsg = '';
    catList=[];
    selectedVnf=[];
    catalogName;
    model: any = {};
    private vnfListOptions=[];
    private catalogsOptions=[];
    private vnfList: any[] = [];
    private catalogs;
    catalogId;body2=[];
    vnfCat = [];
    statusMsg;
    ngOnInit(){  
        this.authenticationService.apiCallGet('catalog/'+this.statusservice.getCatalogDetails().id+'/vnfMinus').subscribe(
            (res) => {
                    for(var vnf of res){
                            this.vnfListOptions.push({ id: vnf.id, name: vnf.name});
                        }
             },
            (err) => {   this.statusMsg=err.json().error;
                    }
        );
        this.catalogName = this.statusservice.getCatalogDetails().name;
        this.statusservice.breadcrumb(("Associate VNF"));
    }
    constructor(private authenticationService: AuthenticationService,private statusservice:StatusService,private router:Router) {}
    asVnfSubmit(){
        if(this.model.vnfList==undefined){
            this.statusMsg="Please Select a VNF to associate"
        }else{
            for(var i=0;i<this.model.vnfList.length;i++){
                this.body2.push({"id":this.model.vnfList[i]});
            }
            var urlparam=this.statusservice.getCatalogDetails().id;
            this.authenticationService.associateVnf(urlparam,this.body2).subscribe(
            (res) =>{
                        this.router.navigateByUrl('/home/detail');   
                    },
            (err) => {
                        this.statusMsg=err.json().error;                                          
                    }
            );
        }
    }
    hideStatusMsg(){
            this.statusMsg=false;
    }

    goBack(){
        this.router.navigateByUrl('/home/detail'); 
    }
}