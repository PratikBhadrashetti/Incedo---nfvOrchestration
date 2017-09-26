import { Injectable } from '@angular/core';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class StatusService {
	status;selectedVnf;catName;catId;vnfCatalogDet;
  launchedVnfDet;currentUserRole;breadCrumb:any;userDetails;
  passwordMessage='';Img;
  breadCrumbChange: Subject<string> = new Subject<string>();
  modifyCatalog;
  modifyCatalogChange: Subject<boolean> = new Subject<boolean>();
  vnfList;setvalue;setid;setobj;setstatusobj;
  vnfListChange: Subject<boolean> = new Subject<boolean>();

  loadingImageChange: Subject<boolean> = new Subject<boolean>();
  constructor() {this.breadCrumb=' ';
                  this.Img=false;
                  this.modifyCatalog=true; 
                  this.vnfList=true;
                }

  setStatus(status){
  	this.status=status;
  }

  getStatus(){
  	return this.status;
  }

  setVnf(vnfList){
  	this.selectedVnf=vnfList;
  }

  getVnf(){
  	return this.selectedVnf;
  }
  getCatalogDetails(){
    return {"id":this.catId,"name":this.catName}
  }
  setCatalogDetails(name,id){
    this.catName = name;
    this.catId = id;
  }

  setVnfCatalogDetails(vnfList){
    this.vnfCatalogDet =vnfList;
  }
  getVnfCatalogDetails(){
    return this.vnfCatalogDet;
  }

  getLaunchedVnfDet(){
     return this.launchedVnfDet;
  }
  setLaunchedVnfDet(launchedVnfDet){
    this.launchedVnfDet = launchedVnfDet;
  }
  getCurrentUserRole(){
    return this.currentUserRole;
  }
  setCurrentUserRole(currentUserRole){
    this.currentUserRole = currentUserRole;
  }
  getUserDetails(){
    return this.userDetails;
  }
  setUserDetails(userDetails){
    this.userDetails = userDetails;
  }
  breadcrumb(bread){
    this.breadCrumb=bread;
    this.breadCrumbChange.next(this.breadCrumb);
    
  }
  loadingImg(img){
    this.Img=img;
    this.loadingImageChange.next(this.Img);
  }

  setPasswordChangeMessage(message){
    this.passwordMessage=message;
  }

  getPasswordChangeMessage(){
    return this.passwordMessage;
  }

  modifyCatalogStatus(modifycatalog){
    this.modifyCatalog =modifycatalog;
    this.modifyCatalogChange.next(this.modifyCatalog);

  }
  vnfListStatus(vnflist){
    this.vnfList =vnflist;
    this.vnfListChange.next(this.vnfList);
  }
  setValue(val){
    this.setvalue=val;
  }
 getValue(){
    return this.setvalue;
  }
 setId(val){
    this.setid=val;
  }
 getId(){
    return this.setid;
  }
 setObj(obj){
   this.setobj=obj;
 }
 getObj(){
   return  this.setobj;
 }
  setstatusObj(obj){
   this.setstatusobj=obj;
 }
 getstatusObj(){
   return  this.setstatusobj;
 }
}
