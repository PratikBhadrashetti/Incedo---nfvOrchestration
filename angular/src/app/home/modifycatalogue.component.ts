import {Component,ChangeDetectorRef} from '@angular/core';
import { Http, Headers, Response,URLSearchParams } from '@angular/http';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
import {Router} from '@angular/router';
import * as GlobalVariable from "../_services/constants";
@Component({
 selector:'modify-catalogue',
 templateUrl:'./modifycatalogue.component.html',
 styleUrls:['./catalogue.css']
})

export class modifyCatalogueComponent{
    model: any = {};
    modifyCatalog;
    catId;
    catName;
    catDesc;
    catIcon;
    modCatMsg = '';
    err = false;
    catList=[];
    viewCatMsg = '';
    catDetMsg='';
    users;
    catImage;
    myFile;
    statusMsg;
    subscription;
    vimid;
    constructor(private authenticationService: AuthenticationService,private http: Http,
                private statusservice:StatusService,private router:Router,
                private changeDetectorRef: ChangeDetectorRef) {
      		this.subscription = this.statusservice.modifyCatalogChange.subscribe((value)=>{ 
            	this.modifyCatalog = value; 
        }); 
    }
  
    private userOptions =[]; 
    ngOnInit() {
        this.statusservice.breadcrumb("List Catalog");
        this.vimid=this.statusservice.getId();
		this.authenticationService.getCatalog(this.vimid).subscribe(
            (res) =>{     
                        this.catList = res;
                        localStorage.setItem('catalogueList',JSON.stringify(this.catList));
                            for(var cat of this.catList){
                                cat.image = GlobalVariable.BASE_API_URL+'catalog/'+cat.id+'/logo';
                            }
                },
            (error)=>{this.statusMsg=error.json().error;
                    }
            );
       this.authenticationService.apiCallGet('user').subscribe(
            (res) =>{     
                        for(var user of res){
                            this.userOptions.push({ id: user.id, name: user.name});
                        }
                },
            (error)=>{  this.statusMsg=error.json().error;
                           }
            );
    }
    setRouter(cat){
        this.statusservice.breadcrumb("Modify Catalog - "+cat.name);
        this.statusservice.modifyCatalogStatus(true);      
        this.model.catName = cat.name;
        this.model.catDesc = cat.description;
        this.catId = cat.id;
        this.catImage = GlobalVariable.BASE_API_URL+'catalog/'+this.catId+'/logo';
        var usrLst = [];
        this.model.usersList =[];
        this.authenticationService.apiCallGet('/catalog/'+cat.id+'/user').subscribe(
                (res) =>{     
                            for(var user of res){
                                var intId = parseInt(user.id);
                                usrLst.push(intId);                            
                            }
                            this.model.usersList = usrLst;
                    },
                (error)=>{  this.statusMsg=error.json().error;
                        }
        );
    }
    modifyCatSubmit(){
        var userInp = [];
        for(var i=0;i<this.model.usersList.length;i++){
                userInp.push({"id":this.model.usersList[i]});
            }
        var body= JSON.stringify({
                                    'name':this.model.catName,
                                    'description':this.model.catDesc
                                    });        
        this.authenticationService.catalogPatchCall(body,this.catId).subscribe(
            (res) =>{
                       if(res){
                            if(typeof(this.myFile)!=="undefined"){
                                let file: File = this.myFile[0];
                                let formData:FormData = new FormData();
                                formData.append('icon', file, file.name);
                                this.authenticationService.logoPatchCall(formData,this.catId).subscribe(
                                    (res) =>{
                                         if(res){
                                            this.userCatalogCall();
                                        }else{
                                            this.modCatMsg = "Error in image upload"; 
                                            this.statusservice.setStatus(this.modCatMsg);
                                        }
                                    },
                                    (err) =>{this.statusMsg=err.json().error;}
                                );
                            }else{
                               this.userCatalogCall();                        
                            }
                        }else{
                            this.modCatMsg = "Error in catalog updation";
                            this.statusservice.setStatus(this.modCatMsg);
                        }
                    },
            (err) => {
                    this.statusMsg=err.json().error;
                    }
        );
    }
    userCatalogCall(){
        if(this.model.usersList==undefined || this.model.usersList.length==0){
            var userInp = [];
        }else{
            var userInp = [];
            for(var i=0;i<this.model.usersList.length;i++){
                userInp.push({"id":this.model.usersList[i]});
            }
        }
        this.authenticationService.userToCatalogPatch(this.catId,JSON.stringify(userInp)).subscribe(
                (res) =>{
                    if(res){
                        this.modCatMsg = 'Catalog updated successfully';     
                        this.router.navigateByUrl('/home/catalogue');
                    }else{
                        this.modCatMsg = "Error in user to catalog mapping"; 
                        this.statusservice.setStatus(this.modCatMsg);
                    }
                },
                (err) =>{this.statusMsg=err.json().error;}
            );
    }
    onChange(event){        
          this.myFile = event.target.files;  
          this.readFiles(event.target.files); 
    }
    readFile(file, reader, callback){
        // Set a callback funtion to fire after the file is fully loaded
        reader.onload = () => {
          // callback with the results
          callback(reader.result);
        }
        
        // Read the file
        reader.readAsDataURL(file);
      }
      
      readFiles(files, index=0){
        // Create the file reader
        let reader = new FileReader();
        // If there is a file
        if(index in files){
          // Start reading this file
          this.readFile(files[index], reader, (result) =>{
            // After the callback fires do:
           // this.file_srcs.push(result);
            this.catImage = result;
            //this.readFiles(files, index+1);// Read the next file;
          });
        }else{
          // When all files are done This forces a change detection
          this.changeDetectorRef.detectChanges();
        }
      }
      hideStatusMsg(){
            this.statusMsg=false;
    }
    clickCancel(){
        this.modifyCatalog=false;
        this.statusservice.breadcrumb("List Catalog");
    }
}