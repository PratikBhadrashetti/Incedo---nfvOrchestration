import {Component} from '@angular/core';
import { Http, Headers, Response,URLSearchParams } from '@angular/http';
import {FormControl, FormGroup,FormsModule} from '@angular/forms';
import {AuthenticationService } from '../_services/index';
import 'rxjs/Rx';
import {StatusService } from '../_services/status.service';
import {Router} from '@angular/router';

@Component({
 selector:'register-vnf',
 templateUrl:'./registerVnf.component.html',
 styleUrls:['./catalogue.css']
})

export class RegisterVnfComponent{
    model: any = {};
    myFile;
    statusMsg;
    vimid;
    constructor(private authenticationService: AuthenticationService,private http: Http,
        private statusservice:StatusService,private router:Router) {
     }
    ngOnInit(){
        this.statusservice.breadcrumb("Register");
    }
    onSubmit(){
        if(this.myFile.length > 0){
            this.vimid=this.statusservice.getId();
            let file: File = this.myFile[0];
            let formData:FormData = new FormData();
            formData.append('template',file, file.name);
            this.authenticationService.registervnfd(formData,this.vimid).subscribe(
            //this.authenticationService.registerVnf(formData).subscribe(
                (res) =>{
                    this.router.navigateByUrl('/home/catalogue');
                },
                (err) =>{
                        this.statusMsg=err.json().error;
                        }
            );
        }
    }
    hideStatusMsg(){
            this.statusMsg=false;
    }
    cancelFn(){
        this.router.navigateByUrl('/home/catalogue');
    }
    onChange(event){        
          this.myFile = event.target.files; 
    }

}