import {Component} from '@angular/core';
import { Http, Headers, Response,URLSearchParams } from '@angular/http';
import {FormControl, FormGroup,FormsModule} from '@angular/forms';
import {AuthenticationService } from '../_services/index';
import 'rxjs/Rx';
import {StatusService } from '../_services/status.service';
import {UtilityService } from '../_services/utility';
import {Router} from '@angular/router';

@Component({
 selector:'add-catalogue',
 templateUrl:'./addcatalogue.component.html'
//  styleUrls:['./catalogue.css']
})

export class addCatalogueComponent{
    model: any = {};
    myFile;
    err = false;
    users;
    selectedItem=[];
    private userOptions =[];  
    catCreateMsg = '';
    icon ='';
    statusMsg;
    disable=true;
    vimobj;
    body;
    constructor(private authenticationService: AuthenticationService,private http: Http,
        private statusservice:StatusService,private utilityservice:UtilityService,private router:Router) {
     }
    ngOnInit(){
        this.statusservice.breadcrumb("Add Catalog");
         this.authenticationService.apiCallGet('user').subscribe(
            (res) =>{     
                        for(var user of res){
                            this.userOptions.push({ id: user.id, name: user.name});
                        }
                },
            (error)=>{
                        this.statusMsg=error.json().error;
                      }
            );
    }
    onSubmit(){
         this.vimobj=this.statusservice.getObj();
          
        if(this.model.tenant==="private"){
            //this.statusservice.getUserDetails();
            this.body= {
                                    'name':this.model.catalogName,
                                    'description':this.model.desc,
                                    'tenant': this.statusservice.getUserDetails().tenant,
                                    'vim':this.vimobj
                                    }; 
        }else{
            this.body= {
                                    'name':this.model.catalogName,
                                    'description':this.model.desc,
                                     'vim':this.vimobj
                                    }; 
        }
        
      //  this.utilityservice.objectMix(this.vimobj, this.body);
        this.authenticationService.catalogPostCall(this.body).subscribe(
                (res) =>{
                           if(!res){
                                this.err = true;
                                this.catCreateMsg = "Error in catalog creation";
                                this.statusservice.setStatus(this.catCreateMsg);
                            }else{
                                if(this.myFile.length > 0){
                                    let file: File = this.myFile[0];
                                    let formData:FormData = new FormData();
                                    formData.append('icon',file, file.name);
                                    var id = res.id;
                                    this.authenticationService.logoPatchCall(formData,id).subscribe(
                                        (res) =>{
                                            if(res){
                                                if(this.model.usersList==undefined || this.model.usersList.length==0){
                                                    this.catCreateMsg = "catalog added successfully"; 
                                                    this.router.navigateByUrl('/home/catalogue');
                                                }else{
                                                var userInp = [];
                                                for(var i=0;i<this.model.usersList.length;i++){
                                                        userInp.push({"id":this.model.usersList[i]});
                                                    }
                                                this.authenticationService.userToCatalogPatch(id,JSON.stringify(userInp)).subscribe(
                                                    (res) =>{
                                                        if(res){
                                                            this.catCreateMsg = "catalog added successfully"; 
                                                            this.router.navigateByUrl('/home/catalogue');
                                                        }else{
                                                            this.catCreateMsg = "Error in user to catalog mapping"; 
                                                            this.statusservice.setStatus(this.catCreateMsg);
                                                        }
                                                    },
                                                    (err) =>{
                                                        this.statusMsg=err.json().error;
                                                        
                                                    });
                                                }
                                            }else{
                                                this.catCreateMsg = "Error in image upload"; 
                                                this.statusservice.setStatus(this.catCreateMsg);
                                            }
                                        },
                                        (err) =>{
                                            this.catCreateMsg = "Error in image upload"; 
                                            this.statusservice.setStatus(this.catCreateMsg);
                                        }
                                    );
                                }
                            }
                        },
            (err) => {
                        this.statusMsg=err.json().error;
                    }
            );
    }
    onChange(event){        
          this.myFile = event.target.files; 
          if(this.myFile.length > 0){
            this.disable = false;      
          }else{
            this.disable = true; 
          } 
    }
    hideStatusMsg(){
            this.statusMsg=false;
    }
    cancelFn(){
        this.router.navigateByUrl('/home/catalogue');
    }
}